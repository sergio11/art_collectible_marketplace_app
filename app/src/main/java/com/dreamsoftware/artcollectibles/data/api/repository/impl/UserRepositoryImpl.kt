package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.SaveUserInfoMapper
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
 * @param saveUserInfoMapper
 * @param authUserMapper
 */
internal class UserRepositoryImpl(
    private val authDataSource: IAuthDataSource,
    private val userDataSource: IUsersDataSource,
    private val storageDataSource: IStorageDataSource,
    private val preferencesDataSource: IPreferencesDataSource,
    private val userInfoMapper: UserInfoMapper,
    private val saveUserInfoMapper: SaveUserInfoMapper,
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
        val authUser = authDataSource.signIn(authRequest.email, authRequest.password)
        Log.d("USER_REPO", "authUser.photoUrl -> ${authUser.photoUrl} CALLED!")
        preferencesDataSource.saveAuthUserUid(authUser.uid)
        authUserMapper.mapInToOut(authUser)
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to sign in user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun signIn(authRequest: ExternalProviderAuthRequest): AuthUser = try {
        val authUser = authDataSource.signInWithExternalProvider(authRequest.accessToken, authRequest.socialAuthTypeEnum)
        Log.d("USER_REPO", "authUser.photoUrl -> ${authUser.photoUrl} CALLED!")
        preferencesDataSource.saveAuthUserUid(authUser.uid)
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
    override suspend fun save(userInfo: SaveUserInfo) = try {
        userDataSource.save(saveUserInfoMapper.mapInToOut(userInfo))
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw UserDataException("An error occurred when trying to create the user", ex)
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