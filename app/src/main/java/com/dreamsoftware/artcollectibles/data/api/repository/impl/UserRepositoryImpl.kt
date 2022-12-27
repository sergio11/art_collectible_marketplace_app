package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.AuthRequest
import com.dreamsoftware.artcollectibles.domain.models.AuthUser
import com.dreamsoftware.artcollectibles.domain.models.UserInfo

internal class UserRepositoryImpl(
    private val authDataSource: IAuthDataSource,
    private val userDataSource: IUsersDataSource,
    private val preferencesDataSource: IPreferencesDataSource,
    private val userInfoMapper: UserInfoMapper,
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
        throw UserDataException("An error occurred when trying to sign in user", ex)
    }

    @Throws(UserDataException::class)
    override suspend fun save(userInfo: UserInfo) = try {
        userDataSource.save(userInfoMapper.mapOutToIn(userInfo))
    } catch (ex: Exception) {
        throw UserDataException("An error occurred when trying to save the user", ex)
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