package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.ISecretRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase
import com.dreamsoftware.artcollectibles.utils.AppEventBus
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import javax.inject.Inject

/**
 * @param userRepository
 * @param preferenceRepository
 * @param secretRepository
 * @param applicationAware
 * @param appEventBus
 */
class RestoreUserAuthenticatedSessionUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val secretRepository: ISecretRepository,
    private val applicationAware: IApplicationAware,
    private val appEventBus: AppEventBus
): BaseUseCase<Boolean>() {
    override suspend fun onExecuted(): Boolean =
        with(userRepository) {
            isAuthenticated() && run {
                applicationAware.setUserSecret(secretRepository.get(getUserAuthenticatedUid()))
                preferenceRepository.getAuthUserUid().isNotBlank() && run {
                    appEventBus.invokeEvent(AppEventBus.AppEvent.SIGN_IN)
                    true
                }
            }
        }
}