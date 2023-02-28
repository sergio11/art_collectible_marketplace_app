package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.SaveUserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFollowersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * User Repository Impl
 * @param authDataSource
 * @param userDataSource
 * @param storageDataSource
 * @param followerDataSource
 * @param preferencesDataSource
 * @param userInfoMapper
 * @param saveUserInfoMapper
 * @param authUserMapper
 */
internal class UserRepositoryImpl(
    private val authDataSource: IAuthDataSource,
    private val userDataSource: IUsersDataSource,
    private val storageDataSource: IStorageDataSource,
    private val followerDataSource: IFollowersDataSource,
    private val preferencesDataSource: IPreferencesDataSource,
    private val userInfoMapper: UserInfoMapper,
    private val saveUserInfoMapper: SaveUserInfoMapper,
    private val authUserMapper: AuthUserMapper
) : IUserRepository {

    private companion object {
        const val PROFILE_PHOTO_DIRECTORY = "userPhotos"
        const val PROFILE_PHOTO_FILE_NAME_TEMPLATE = "{uid}_profile_picture"
    }

    @Throws(CheckAuthenticatedException::class)
    override suspend fun isAuthenticated(): Boolean = try {
        authDataSource.isAuthenticated()
    } catch (ex: Exception) {
        throw CheckAuthenticatedException(
            "An error occurred when trying to check if user is already authenticated",
            ex
        )
    }

    @Throws(SignInException::class)
    override suspend fun signIn(authRequest: AuthRequest): AuthUser = try {
        with(authRequest) {
            val authUser = authDataSource.signIn(email, password)
            authUserMapper.mapInToOut(authUser)
        }
    } catch (ex: Exception) {
        throw SignInException("An error occurred when trying to sign in user", ex)
    }

    @Throws(SignInException::class)
    override suspend fun signIn(authRequest: ExternalProviderAuthRequest): AuthUser = try {
        with(authRequest) {
            val authUser = authDataSource.signInWithExternalProvider(
                accessToken,
                externalProviderAuthTypeEnum
            )
            authUserMapper.mapInToOut(authUser)
        }
    } catch (ex: Exception) {
        throw SignInException("An error occurred when trying to sign in user", ex)
    }

    @Throws(SignUpException::class)
    override suspend fun signUp(email: String, password: String): AuthUser = try {
        val authUser = authDataSource.signUp(email, password)
        authUserMapper.mapInToOut(authUser)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw SignUpException("An error occurred when trying to sign up user", ex)
    }

    @Throws(GetDetailException::class)
    override suspend fun get(uid: String): UserInfo = withContext(Dispatchers.Default) {
        try {
            val userInfoDeferred = async { userDataSource.getById(uid) }
            val countFollowersDeferred = async { followerDataSource.countFollowers(uid) }
            val countFollowingDeferred = async { followerDataSource.countFollowing(uid) }
            userInfoMapper.mapInToOut(
                UserInfoMapper.InputData(
                    user = userInfoDeferred.await(),
                    followers = countFollowersDeferred.await(),
                    following = countFollowingDeferred.await()
                )
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetDetailException("An error occurred when trying to get the user information", ex)
        }
    }

    @Throws(GetDetailException::class)
    override suspend fun getByAddress(userAddress: String): UserInfo = withContext(Dispatchers.Default) {
        try {
            val userInfo = userDataSource.getByAddress(userAddress)
            val countFollowersDeferred = async { followerDataSource.countFollowers(userInfo.uid) }
            val countFollowingDeferred = async { followerDataSource.countFollowing(userInfo.uid) }
            userInfoMapper.mapInToOut(
                UserInfoMapper.InputData(
                    user = userInfo,
                    followers = countFollowersDeferred.await(),
                    following = countFollowingDeferred.await()
                )
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetDetailException("An error occurred when trying to get the user information", ex)
        }
    }

    @Throws(SaveUserException::class)
    override suspend fun save(userInfo: UserInfo) = try {
        userDataSource.save(saveUserInfoMapper.mapInToOut(userInfo))
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw SaveUserException("An error occurred when trying to create the user", ex)
    }

    @Throws(UpdateProfilePictureException::class)
    override suspend fun updateProfilePicture(uid: String, fileUri: String): String = try {
        val userInfo = userDataSource.getById(uid)
        val fileName = PROFILE_PHOTO_FILE_NAME_TEMPLATE.replace("{uid}", userInfo.uid)
        if (!userInfo.photoUrl.isNullOrBlank()) {
            storageDataSource.remove(PROFILE_PHOTO_DIRECTORY, fileName)
        }
        storageDataSource.save(PROFILE_PHOTO_DIRECTORY, fileName, fileUri).toString().also {
            with(userInfo) {
                userDataSource.save(
                    SaveUserDTO(
                        uid = uid,
                        name = name,
                        walletAddress = walletAddress,
                        photoUrl = it
                    )
                )
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw UpdateProfilePictureException("An error occurred when trying to save file", ex)
    }

    @Throws(CloseSessionException::class)
    override suspend fun closeSession() {
        try {
            authDataSource.closeSession()
        } catch (ex: Exception) {
            throw CloseSessionException("An error occurred when trying to close user session", ex)
        }
    }

    @Throws(SearchUserException::class)
    override suspend fun search(term: String?): Iterable<UserInfo> = withContext(Dispatchers.IO) {
        try {
            val users = term?.let {
                userDataSource.findUsersByName(it)
            } ?: userDataSource.getAll()
            users.map {
                val countFollowersDeferred = async { followerDataSource.countFollowers(it.uid) }
                val countFollowingDeferred = async { followerDataSource.countFollowing(it.uid) }
                userInfoMapper.mapInToOut(UserInfoMapper.InputData(
                    user = it,
                    followers = countFollowersDeferred.await(),
                    following = countFollowingDeferred.await()
                ))
            }
        } catch (ex: Exception) {
            throw SearchUserException("An error occurred when trying to find users", ex)
        }
    }

    @Throws(FollowUserException::class)
    override suspend fun followUser(userUid: String) {
        withContext(Dispatchers.IO) {
            val authUserUid = preferencesDataSource.getAuthUserUid()
            followerDataSource.follow(authUserUid, userUid)
        }
    }

    @Throws(UnFollowUserException::class)
    override suspend fun unfollowUser(userUid: String) {
        withContext(Dispatchers.IO) {
            val authUserUid = preferencesDataSource.getAuthUserUid()
            followerDataSource.unfollow(authUserUid, userUid)
        }
    }

    @Throws(CheckFollowersUserException::class)
    override suspend fun isFollowingTo(userUid: String): Boolean =
        withContext(Dispatchers.IO) {
            val authUserUid = preferencesDataSource.getAuthUserUid()
            followerDataSource.isFollowedBy(userUid, authUserUid)
        }
}