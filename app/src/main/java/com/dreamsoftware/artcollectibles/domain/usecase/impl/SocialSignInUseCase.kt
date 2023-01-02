package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.exception.UserDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.firebase.exception.UserNotFoundException
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

/**
 * Social SignIn Use Case
 * @param userRepository
 * @param walletRepository
 */
class SocialSignInUseCase(
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository,
) : BaseUseCaseWithParams<SocialSignInUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        val authUser = userRepository.signIn(ExternalProviderAuthRequest(accessToken, socialAuthTypeEnum))
        try {
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

    private suspend fun createUser(authUser: AuthUser): UserInfo = with(authUser) {
        val wallet = walletRepository.generate(userUid = uid)
        userRepository.save(
            UserInfo(
                uid = uid,
                name = displayName,
                contact = email,
                photoUrl = photoUrl,
                walletAddress = wallet.address
            )
        )
        userRepository.get(uid)
    }

    data class Params(
        val accessToken: String,
        val socialAuthTypeEnum: SocialAuthTypeEnum
    )
}