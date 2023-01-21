package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.domain.models.*

interface IUserRepository {

    @Throws(UserDataException::class)
    suspend fun isAuthenticated(): Boolean

    @Throws(UserDataException::class)
    suspend fun signIn(authRequest: AuthRequest): AuthUser

    @Throws(UserDataException::class)
    suspend fun signIn(authRequest: ExternalProviderAuthRequest): AuthUser

    @Throws(UserDataException::class)
    suspend fun signUp(email: String, password: String): AuthUser

    @Throws(UserDataException::class)
    suspend fun get(uid: String): UserInfo

    @Throws(UserDataException::class)
    suspend fun save(userInfo: UserInfo)

    @Throws(UserDataException::class)
    suspend fun updateProfilePicture(uid: String, fileUri: String): String

    @Throws(UserDataException::class)
    suspend fun closeSession()

    @Throws(UserDataException::class)
    suspend fun search(term: String? = null): Iterable<UserInfo>
}