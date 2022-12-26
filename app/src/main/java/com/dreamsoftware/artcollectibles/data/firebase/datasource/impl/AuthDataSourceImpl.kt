package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IAuthDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.FirebaseException
import com.dreamsoftware.artcollectibles.data.firebase.mapper.FirebaseUserMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.AuthUserDTO
import com.dreamsoftware.artcollectibles.domain.models.AuthTypeEnum
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Auth Data Source Implementation
 * @param firebaseUserMapper
 * @param firebaseAuth
 */
internal class AuthDataSourceImpl(
    private val firebaseUserMapper: FirebaseUserMapper,
    private val firebaseAuth: FirebaseAuth
) : IAuthDataSource {

    override suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        firebaseAuth.currentUser != null
    }

    @Throws(FirebaseException::class)
    override suspend fun signInWithAccessToken(
        accessToken: String,
        authTypeEnum: AuthTypeEnum
    ): AuthUserDTO = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signInWithCredential(
                getCredentials(accessToken, authTypeEnum)
            ).await()
                ?.user?.let { firebaseUserMapper.mapInToOut(it) }
                ?: throw IllegalStateException("Auth user cannot be null")
        } catch (ex: Exception) {
            throw FirebaseException("Login failed", ex)
        }
    }

    @Throws(FirebaseException::class)
    override suspend fun closeSession() = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.signOut()
        } catch (ex: Exception) {
            throw FirebaseException("Logout failed", ex)
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