package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.FirebaseException
import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.AuthTypeEnum

interface IAuthDataSource {

    suspend fun isAuthenticated(): Boolean

    /**
     * Sign In With Access Token
     * @param accessToken
     * @param authTypeEnum
     */
    @Throws(FirebaseException::class)
    suspend fun signInWithAccessToken(accessToken: String, authTypeEnum: AuthTypeEnum): AuthUserDTO

    @Throws(FirebaseException::class)
    suspend fun closeSession()
}