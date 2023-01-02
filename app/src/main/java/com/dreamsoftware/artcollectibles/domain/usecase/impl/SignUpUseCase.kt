package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

/**
 * Sign Use case
 * @param userRepository
 * @param walletRepository
 */
class SignUpUseCase(
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository
): BaseUseCaseWithParams<SignUpUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        val authUser = userRepository.signUp(email, password)
        val wallet = walletRepository.generate(userUid = authUser.uid)
        with(authUser) {
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
    }

    data class Params(
        val email: String,
        val password: String
    )
}