package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.ISecretRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import com.dreamsoftware.artcollectibles.utils.IApplicationAware

/**
 * Sign Use case
 * @param userRepository
 * @param walletRepository
 * @param secretRepository
 * @param preferenceRepository
 * @param applicationAware
 */
class SignUpUseCase(
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository,
    private val secretRepository: ISecretRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val applicationAware: IApplicationAware
): BaseUseCaseWithParams<SignUpUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        val authUser = userRepository.signUp(email, password)
        applicationAware.setUserSecretKey(key = secretRepository.generate(authUser.uid))
        preferenceRepository.saveAuthUserUid(uid = authUser.uid)
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

    override suspend fun onReverted(params: Params) {
        applicationAware.setUserSecretKey(null)
        preferenceRepository.clearData()
    }

    data class Params(
        val email: String,
        val password: String
    )
}