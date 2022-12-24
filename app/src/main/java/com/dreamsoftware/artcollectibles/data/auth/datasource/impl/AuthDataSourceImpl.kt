package com.dreamsoftware.artcollectibles.data.auth.datasource.impl

import com.dreamsoftware.artcollectibles.data.auth.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.auth.exception.AuthException
import com.dreamsoftware.artcollectibles.data.auth.mapper.AuthUserMapper
import com.dreamsoftware.artcollectibles.domain.models.AuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.AuthUser
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class AuthDataSourceImpl(
    private val authUserMapper: AuthUserMapper,
    private val firebaseAuth: FirebaseAuth
) : IAuthDataSource {

    override suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        firebaseAuth.currentUser != null
    }

    @Throws(AuthException::class)
    override suspend fun signInWithAccessToken(
        accessToken: String,
        authTypeEnum: AuthTypeEnum
    ): AuthUser = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signInWithCredential(
                getCredentials(accessToken, authTypeEnum)
            ).await()
                ?.user?.let { authUserMapper.mapInToOut(it) }
                ?: throw IllegalStateException("Auth user cannot be null")
        } catch (ex: Exception) {
            throw AuthException("Login failed", ex)
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
     * @param authTypeEnum
     */
    private fun getCredentials(accessToken: String, authTypeEnum: AuthTypeEnum) =
        when (authTypeEnum) {
            AuthTypeEnum.FACEBOOK -> FacebookAuthProvider.getCredential(accessToken)
            AuthTypeEnum.GOOGLE -> GoogleAuthProvider.getCredential(accessToken, null)
        }
}