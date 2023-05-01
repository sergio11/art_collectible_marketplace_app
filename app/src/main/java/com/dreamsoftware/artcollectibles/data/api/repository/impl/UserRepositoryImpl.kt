package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.SaveUserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFollowersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.data.memory.datasource.IUserMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials

/**
 * User Repository Impl
 * @param authDataSource
 * @param userDataSource
 * @param storageDataSource
 * @param followerDataSource
 * @param preferencesDataSource
 * @param artCollectibleDataSource
 * @param artMarketplaceBlockchainDataSource
 * @param walletRepository
 * @param userMemoryDataSource
 * @param userCredentialsMapper
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
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val walletRepository: IWalletRepository,
    private val userMemoryDataSource: IUserMemoryDataSource,
    private val userCredentialsMapper: UserCredentialsMapper,
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

    @Throws(CheckAuthenticatedException::class)
    override suspend fun getUserAuthenticatedUid(): String = try {
        authDataSource.getUserAuthenticatedUid()
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
    override suspend fun get(uid: String, fullDetail: Boolean): UserInfo = withContext(Dispatchers.Default) {
        try {
            if(fullDetail) {
                with(userMemoryDataSource) {
                    try {
                        findByKey(uid)
                    } catch (ex: CacheException) {
                        fetchUserByUid(uid, true).also {
                            save(uid, it)
                        }
                    }
                }
            } else {
                fetchUserByUid(uid, false)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetDetailException("An error occurred when trying to get the user information", ex)
        }
    }

    @Throws(GetDetailException::class)
    override suspend fun getByAddress(userAddress: String, fullDetail: Boolean): UserInfo = withContext(Dispatchers.Default) {
        try {
            if(fullDetail) {
                with(userMemoryDataSource) {
                    try {
                        findByKey(userAddress)
                    } catch (ex: CacheException) {
                        fetchUserByAddress(userAddress, true).also {
                            save(userAddress, it)
                        }
                    }
                }
            } else {
                fetchUserByAddress(userAddress, false)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw GetDetailException("An error occurred when trying to get the user information", ex)
        }
    }

    @Throws(SaveUserException::class)
    override suspend fun save(userInfo: UserInfo) = try {
        userDataSource.save(saveUserInfoMapper.mapInToOut(userInfo).also {
            with(userMemoryDataSource) {
                delete(it.uid)
                delete(it.walletAddress)
            }
        })
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
                ).also {
                    with(userMemoryDataSource) {
                        delete(uid)
                        delete(walletAddress)
                    }
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw UpdateProfilePictureException("An error occurred when trying to save file", ex)
    }

    @Throws(CloseSessionException::class)
    override suspend fun closeSession() {
        try {
            userMemoryDataSource.delete()
            authDataSource.closeSession()
        } catch (ex: Exception) {
            throw CloseSessionException("An error occurred when trying to close user session", ex)
        }
    }

    @Throws(SearchUserException::class)
    override suspend fun search(term: String?): Iterable<UserInfo> = withContext(Dispatchers.IO) {
        try {
            val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
            val users = term?.let {
                userDataSource.findUsersByName(it)
            } ?: userDataSource.getAll()
            users.map { mapToUserInfo(it, credentials, true) }
        } catch (ex: Exception) {
            throw SearchUserException("An error occurred when trying to find users", ex)
        }
    }

    @Throws(FollowUserException::class)
    override suspend fun followUser(userUid: String) {
        withContext(Dispatchers.IO) {
            try {
                val authUserUid = preferencesDataSource.getAuthUserUid()
                followerDataSource.follow(authUserUid, userUid).also {
                    with(userMemoryDataSource) {
                        delete(authUserUid)
                        delete(userUid)
                    }
                }
            }  catch (ex: Exception) {
                throw FollowUserException("An error occurred when trying to follow to user", ex)
            }
        }
    }

    @Throws(UnFollowUserException::class)
    override suspend fun unfollowUser(userUid: String) {
        withContext(Dispatchers.IO) {
            try {
                val authUserUid = preferencesDataSource.getAuthUserUid()
                followerDataSource.unfollow(authUserUid, userUid).also {
                    with(userMemoryDataSource) {
                        delete(authUserUid)
                        delete(userUid)
                    }
                }
            } catch (ex: Exception) {
                throw UnFollowUserException("An error occurred when trying to unfollow to user", ex)
            }
        }
    }

    @Throws(CheckFollowersUserException::class)
    override suspend fun isFollowingTo(userUid: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val authUserUid = preferencesDataSource.getAuthUserUid()
                followerDataSource.isFollowedBy(userUid, authUserUid)
            } catch (ex: Exception) {
                throw CheckFollowersUserException("An error occurred when trying to check followers", ex)
            }
        }

    @Throws(GetFollowersUserException::class)
    override suspend fun getFollowers(userId: String): Iterable<UserInfo> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
                val followersUidList = followerDataSource.getFollowers(userId)
                userDataSource.getById(followersUidList).map { mapToUserInfo(it, credentials, true) }
            } catch (ex: Exception) {
                throw GetFollowersUserException("An error occurred when trying to get followers", ex)
            }
        }

    @Throws(GetFollowingUserException::class)
    override suspend fun getFollowing(userId: String): Iterable<UserInfo> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
                val followingUidList = followerDataSource.getFollowing(userId)
                userDataSource.getById(followingUidList).map { mapToUserInfo(it, credentials, true) }
            } catch (ex: Exception) {
                throw GetFollowingUserException("An error occurred when trying to get followers", ex)
            }
        }

    @Throws(GetMostFollowedUsersException::class)
    override suspend fun getMostFollowedUsers(limit: Int): Iterable<UserInfo> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
                followerDataSource.getMostFollowedUsers(limit).map { uid ->
                    Log.d("ART_COLL", "getMostFollowedUsers uid -> $uid")
                    async { userDataSource.getById(uid) }
                }.awaitAll().map {
                    mapToUserInfo(it, credentials, true)
                }
            } catch (ex: Exception) {
                throw GetMostFollowedUsersException("An error occurred when trying to get most followed users", ex)
            }
        }

    private suspend fun mapToUserInfo(userDTO: UserDTO, credentials: Credentials, fullDetail: Boolean) = withContext(Dispatchers.IO) {
        val countFollowersDeferred = async { followerDataSource.countFollowers(userDTO.uid) }
        val countFollowingDeferred = async { followerDataSource.countFollowing(userDTO.uid) }
        val tokensStatisticsByAddressDeferred = async { artCollectibleDataSource.fetchTokensStatisticsByAddress(credentials, userDTO.walletAddress) }
        val walletStatisticsByAddressDeferred = async { artMarketplaceBlockchainDataSource.fetchWalletStatistics(credentials, userDTO.walletAddress) }
        val tokensStatisticsByAddress = tokensStatisticsByAddressDeferred.await()
        val walletStatisticsByAddress = walletStatisticsByAddressDeferred.await()
        userInfoMapper.mapInToOut(
            if(fullDetail) {
                UserInfoMapper.InputData(
                    user = userDTO,
                    followers = countFollowersDeferred.await(),
                    following = countFollowingDeferred.await(),
                    tokensCreatedCount = tokensStatisticsByAddress.countTokensCreator,
                    tokensOwnedCount = tokensStatisticsByAddress.countTokensOwned,
                    tokensBoughtCount = walletStatisticsByAddress.countTokenBought,
                    tokensSoldCount = walletStatisticsByAddress.countTokenSold
                )
            } else {
                UserInfoMapper.InputData(
                    user = userDTO
                )
            }
        )
    }

    private suspend fun fetchUserByUid(uid: String, fullDetail: Boolean): UserInfo = withContext(Dispatchers.IO)  {
        val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
        val userInfo = userDataSource.getById(uid)
        mapToUserInfo(userInfo, credentials, fullDetail)
    }

    private suspend fun fetchUserByAddress(userAddress: String, fullDetail: Boolean): UserInfo = withContext(Dispatchers.IO) {
        val credentials = userCredentialsMapper.mapOutToIn(walletRepository.loadCredentials())
        val userInfo = userDataSource.getByAddress(userAddress)
        mapToUserInfo(userInfo, credentials, fullDetail)
    }
}