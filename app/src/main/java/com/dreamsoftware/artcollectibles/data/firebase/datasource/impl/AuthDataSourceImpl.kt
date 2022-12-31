package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.AuthException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SignInException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SignUpException
import com.dreamsoftware.artcollectibles.data.firebase.mapper.ExternalUserAuthenticatedMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.UserAuthenticatedMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.ExternalAuthTypeEnum
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Auth Data Source Implementation
 * @param externalUserAuthenticatedMapper
 * @param userAuthenticatedMapper
 * @param firebaseAuth
 */
internal class AuthDataSourceImpl(
    private val externalUserAuthenticatedMapper: ExternalUserAuthenticatedMapper,
    private val userAuthenticatedMapper: UserAuthenticatedMapper,
    private val firebaseAuth: FirebaseAuth
) : IAuthDataSource {

    @Throws(AuthException::class)
    override suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.currentUser != null
        } catch (ex: Exception) {
            throw AuthException("An error occurred when trying to check auth state", ex)
        }

    }

    @Throws(SignInException::class)
    override suspend fun signInWithExternalProvider(
        accessToken: String,
        externalAuthTypeEnum: ExternalAuthTypeEnum
    ): AuthUserDTO = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signInWithCredential(
                getCredentials(accessToken, externalAuthTypeEnum)
            ).await()
                ?.user?.let {
                    externalUserAuthenticatedMapper.mapInToOut(
                        Triple(
                            it,
                            accessToken,
                            externalAuthTypeEnum
                        )
                    )
                }
                ?: throw IllegalStateException("Auth user cannot be null")
        } catch (ex: Exception) {
            throw SignInException("Login failed", ex)
        }
    }

    @Throws(SignInException::class)
    override suspend fun signIn(email: String, password: String): AuthUserDTO =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .await()?.user?.let { userAuthenticatedMapper.mapInToOut(it) }
                    ?: throw IllegalStateException("Auth user cannot be null")
            } catch (ex: Exception) {
                throw SignInException("Login failed", ex)
            }
        }

    @Throws(SignUpException::class)
    override suspend fun signUp(email: String, password: String): AuthUserDTO =
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .await()?.user?.let { userAuthenticatedMapper.mapInToOut(it) }
                    ?: throw IllegalStateException("Auth user cannot be null")
            } catch (ex: Exception) {
                throw SignUpException("Sign up failed", ex)
            }
        }

    @Throws(AuthException::class)
    override suspend fun closeSession() = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signOut()
        } catch (ex: Exception) {
            throw AuthException("Logout failed", ex)
        }
    }

    /**
     * Private Methods
     */

    /**
     * Get Credentials
     * @param accessToken
     * @param externalAuthTypeEnum
     */
    private fun getCredentials(accessToken: String, externalAuthTypeEnum: ExternalAuthTypeEnum) =
        when (externalAuthTypeEnum) {
            ExternalAuthTypeEnum.FACEBOOK -> FacebookAuthProvider.getCredential(accessToken)
            ExternalAuthTypeEnum.GOOGLE -> GoogleAuthProvider.getCredential(accessToken, null)
        }
}