package com.dreamsoftware.artcollectibles.data.auth.datasource

import com.dreamsoftware.artcollectibles.data.auth.exception.AuthException
import com.dreamsoftware.artcollectibles.domain.models.AuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.AuthUser

interface IAuthDataSource {

    suspend fun isAuthenticated(): Boolean

    /**
     * Sign In With Access Token
     * @param accessToken
     * @param authTypeEnum
     */
    @Throws(AuthException::class)
    suspend fun signInWithAccessToken(accessToken: String, authTypeEnum: AuthTypeEnum): AuthUser

    @Throws(AuthException::class)
    suspend fun closeSession()
}