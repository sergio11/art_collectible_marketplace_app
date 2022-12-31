package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserNotFoundException
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

/**
 * SignIn Use Case
 * @param userRepository
 * @param walletRepository
 */
class SignInUseCase(
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository,
) : BaseUseCaseWithParams<SignInUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo {
        val authUser = when(params) {
            is AuthParams ->
                userRepository.signIn(
                    AuthRequest(
                        email = params.email,
                        password = params.password
                    )
                )
            is ExternalAuthParams ->
                userRepository.signIn(
                    ExternalProviderAuthRequest(
                        accessToken = params.accessToken,
                        externalAuthTypeEnum = params.externalAuthTypeEnum
                    )
                )
            else -> throw IllegalStateException("Auth method not supported")
        }
        return try {
            userRepository.get(uid = authUser.uid)
        } catch (ex: UserDataException) {
            if (ex.cause is UserNotFoundException) {
                createUser(authUser)
            } else {
                throw ex
            }
        }
    }

    /**
     * Private Methods
     */

    private suspend fun createUser(authUser: AuthUser): UserInfo {
        val userWalletCredentials = walletRepository.generate()
        return with(authUser) {
            userRepository.save(
                SaveUserInfo(
                    uid = uid,
                    name = displayName,
                    contact = email,
                    photoUrl = photoUrl,
                    walletAddress = userWalletCredentials.address
                )
            )
            userRepository.get(uid)
        }
    }

    interface Params

    data class ExternalAuthParams(
        val accessToken: String,
        val externalAuthTypeEnum: ExternalAuthTypeEnum
    ): Params

    data class AuthParams(
        val email: String,
        val password: String
    ): Params
}