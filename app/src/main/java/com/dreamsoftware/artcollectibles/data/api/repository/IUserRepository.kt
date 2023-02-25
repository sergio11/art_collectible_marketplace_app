package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.*

interface IUserRepository {

    @Throws(CheckAuthenticatedException::class)
    suspend fun isAuthenticated(): Boolean

    @Throws(SignInException::class)
    suspend fun signIn(authRequest: AuthRequest): AuthUser

    @Throws(SignInException::class)
    suspend fun signIn(authRequest: ExternalProviderAuthRequest): AuthUser

    @Throws(SignUpException::class)
    suspend fun signUp(email: String, password: String): AuthUser

    @Throws(GetDetailException::class)
    suspend fun get(uid: String): UserInfo

    @Throws(GetDetailException::class)
    suspend fun getByAddress(userAddress: String): UserInfo

    @Throws(SaveUserException::class)
    suspend fun save(userInfo: UserInfo)

    @Throws(UpdateProfilePictureException::class)
    suspend fun updateProfilePicture(uid: String, fileUri: String): String

    @Throws(CloseSessionException::class)
    suspend fun closeSession()

    @Throws(SearchUserException::class)
    suspend fun search(term: String? = null): Iterable<UserInfo>
}