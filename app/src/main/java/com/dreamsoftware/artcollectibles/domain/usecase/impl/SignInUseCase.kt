package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.ISecretRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.AuthRequest
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import com.dreamsoftware.artcollectibles.utils.AppEventBus
import com.dreamsoftware.artcollectibles.utils.IApplicationAware

/**
 * SignIn Use Case
 * @param userRepository
 * @param secretRepository
 * @param preferenceRepository
 * @param applicationAware
 * @param appEventBus
 */
class SignInUseCase(
    private val userRepository: IUserRepository,
    private val secretRepository: ISecretRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val applicationAware: IApplicationAware,
    private val appEventBus: AppEventBus
) : BaseUseCaseWithParams<SignInUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo = with(params) {
        val authUser = userRepository.signIn(AuthRequest(email, password))
        applicationAware.setUserSecret(secretRepository.get(authUser.uid))
        preferenceRepository.saveAuthUserUid(uid = authUser.uid)
        userRepository.get(uid = authUser.uid, fullDetail = false).also {
            appEventBus.invokeEvent(AppEventBus.AppEvent.SIGN_IN)
        }
    }

    override suspend fun onReverted(params: SignInUseCase.Params) {
        userRepository.closeSession()
        preferenceRepository.clearData()
        applicationAware.setUserSecret(null)
    }

    data class Params(
        val email: String,
        val password: String
    )
}