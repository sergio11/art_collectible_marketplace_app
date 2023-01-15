package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.SaveUserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.domain.models.*

/**
 * User Repository Impl
 * @param authDataSource
 * @param userDataSource
 * @param storageDataSource
 * @param userInfoMapper
 * @param saveUserInfoMapper
 * @param authUserMapper
 */
internal class UserRepositoryImpl(
    private val authDataSource: IAuthDataSource,
    private val userDataSource: IUsersDataSource,
    private val storageDataSource: IStorageDataSource,
    private val userInfoMapper: UserInfoMapper,
    private val saveUserInfoMapper: SaveUserInfoMapper,
    private val authUserMapper: AuthUserMapper
) : IUserRepository {

    private companion object {
        const val PROFILE_PHOTO_DIRECTORY = "userPhotos"
        const val PROFILE_PHOTO_FILE_NAME_TEMPLATE = "{uid}_profile_picture.{ext}"
    }

    @Throws(UserDataException::class)
    override suspend fun isAuthenticated(): Boolean = try {
        authDataSource.isAuthenticated()
    } catch (ex: Exception) {
        throw UserDataException(
            "An error occurred when trying to check if user is already authenticated",
            ex
        )
    }

    @Throws(UserDataException::class)
    override suspend fun signIn(authRequest: AuthRequest): AuthUser = try {
        val authUser = authDataSource.signIn(authRequest.email, authRequest.password)
        authUserMapper.mapInToOut(authUser)
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to sign in user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun signIn(authRequest: ExternalProviderAuthRequest): AuthUser = try {
        val authUser = authDataSource.signInWithExternalProvider(
            authRequest.accessToken,
            authRequest.externalProviderAuthTypeEnum
        )
        authUserMapper.mapInToOut(authUser)
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to sign in user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun signUp(email: String, password: String): AuthUser = try {
        val authUser = authDataSource.signUp(email, password)
        authUserMapper.mapInToOut(authUser)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw UserDataException("An error occurred when trying to sign up user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun get(uid: String): UserInfo = try {
        val userInfo = userDataSource.getById(uid)
        userInfoMapper.mapInToOut(userInfo)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw UserDataException("An error occurred when trying to get the user information", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun save(userInfo: UserInfo) = try {
        userDataSource.save(saveUserInfoMapper.mapInToOut(userInfo))
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw UserDataException("An error occurred when trying to create the user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun updateProfilePicture(uid: String, fileUri: String): String = try {
        val userInfo = userDataSource.getById(uid)
        val fileName = PROFILE_PHOTO_FILE_NAME_TEMPLATE.replace("{uid}", userInfo.uid)
            .replace("{ext}", fileUri.substring(fileUri.lastIndexOf(".") + 1))
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
        throw UserDataException("An error occurred when trying to save file", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun closeSession() {
        try {
            authDataSource.closeSession()
        } catch (ex: Exception) {
            throw UserDataException("An error occurred when trying to close user session", ex)
        }
    }
}