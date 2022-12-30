package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.domain.models.*

interface IUserRepository {

    @Throws(UserDataException::class)
    suspend fun isAuthenticated(): Boolean

    @Throws(UserDataException::class)
    suspend fun signIn(authRequest: AuthRequest): AuthUser

    @Throws(UserDataException::class)
    suspend fun get(uid: String): UserInfo

    @Throws(UserDataException::class)
    suspend fun create(userInfo: CreateUserInfo)

    @Throws(UserDataException::class)
    suspend fun update(userInfo: UpdateUserInfo)

    @Throws(UserDataException::class)
    suspend fun closeSession()
}