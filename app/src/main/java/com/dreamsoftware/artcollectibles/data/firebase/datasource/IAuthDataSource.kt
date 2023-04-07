package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.AuthException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SignInException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SignUpException
import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum

interface IAuthDataSource {

    @Throws(AuthException::class)
    suspend fun isAuthenticated(): Boolean

    @Throws(AuthException::class)
    suspend fun getUserAuthenticatedUid(): String

    /**
     * Sign In With External Provider
     * @param accessToken
     * @param externalProviderAuthTypeEnum
     */
    @Throws(SignInException::class)
    suspend fun signInWithExternalProvider(accessToken: String, externalProviderAuthTypeEnum: ExternalProviderAuthTypeEnum): AuthUserDTO

    /**
     * Sign In
     * @param email
     * @param password
     */
    @Throws(SignInException::class)
    suspend fun signIn(email: String, password: String): AuthUserDTO

    /**
     * Sign Up
     * @param email
     * @param password
     */
    @Throws(SignUpException::class)
    suspend fun signUp(email: String, password: String): AuthUserDTO

    @Throws(AuthException::class)
    suspend fun closeSession()
}