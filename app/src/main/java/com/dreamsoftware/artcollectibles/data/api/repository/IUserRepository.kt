package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.domain.models.AuthRequest
import com.dreamsoftware.artcollectibles.domain.models.AuthUser
import com.dreamsoftware.artcollectibles.domain.models.UserInfo

interface IUserRepository {

    @Throws(UserDataException::class)
    suspend fun isAuthenticated(): Boolean

    @Throws(UserDataException::class)
    suspend fun signIn(authRequest: AuthRequest): AuthUser

    @Throws(UserDataException::class)
    suspend fun get(uid: String): UserInfo

    @Throws(UserDataException::class)
    suspend fun save(userInfo: UserInfo)

    @Throws(UserDataException::class)
    suspend fun closeSession()
}