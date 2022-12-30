package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.CreateUserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UpdateUserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.*

/**
 * User Repository Impl
 * @param authDataSource
 * @param userDataSource
 * @param storageDataSource
 * @param preferencesDataSource
 * @param userInfoMapper
 * @param createUserInfoMapper
 * @param updateUserInfoMapper
 * @param authUserMapper
 */
internal class UserRepositoryImpl(
    private val authDataSource: IAuthDataSource,
    private val userDataSource: IUsersDataSource,
    private val storageDataSource: IStorageDataSource,
    private val preferencesDataSource: IPreferencesDataSource,
    private val userInfoMapper: UserInfoMapper,
    private val createUserInfoMapper: CreateUserInfoMapper,
    private val updateUserInfoMapper: UpdateUserInfoMapper,
    private val authUserMapper: AuthUserMapper
): IUserRepository {

    @Throws(UserDataException::class)
    override suspend fun isAuthenticated(): Boolean = try {
        authDataSource.isAuthenticated() &&
                preferencesDataSource.getAuthUserUid().isNotBlank()
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to check if user is already authenticated", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun signIn(authRequest: AuthRequest): AuthUser = try {
        val authUser = authDataSource.signInWithAccessToken(authRequest.accessToken, authRequest.authTypeEnum)
        Log.d("USER_REPO", "authUser.photoUrl -> ${authUser.photoUrl}")
        preferencesDataSource.saveAuthUserUid(authUser.uid)
        authUserMapper.mapInToOut(authUser)
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to sign in user", ex)
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
    override suspend fun create(userInfo: CreateUserInfo) = try {
        userDataSource.create(createUserInfoMapper.mapInToOut(userInfo.let {
            Log.d("USER_REPO", "it.photoUrl -> ${it.photoUrl}")
            if(!it.photoUrl.isNullOrBlank()) {
                val fileSavedUri = storageDataSource.save("${userInfo.uid}_profile_photo", it.photoUrl)
                Log.d("USER_REPO", "fileSavedUri.toString() -> $fileSavedUri")
                it.copy(photoUrl = fileSavedUri.toString())
            } else {
                it
            }
        }))
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to create the user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun update(userInfo: UpdateUserInfo) = try {
        userDataSource.update(updateUserInfoMapper.mapInToOut(userInfo.let {
            if(!it.photoUrl.isNullOrBlank()) {
                val fileSavedUri = storageDataSource.save("${userInfo.uid}_profile_photo", it.photoUrl)
                it.copy(photoUrl = fileSavedUri.toString())
            } else {
                it
            }
        }))
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to update the user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun closeSession() {
        try {
            authDataSource.closeSession()
            preferencesDataSource.clearData()
        } catch (ex: Exception) {
            throw UserDataException("An error occurred when trying to close user session", ex)
        }
    }
}