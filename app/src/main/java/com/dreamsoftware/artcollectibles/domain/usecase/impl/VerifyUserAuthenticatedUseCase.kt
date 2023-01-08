package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase
import javax.inject.Inject

/**
 * @param userRepository
 * @param preferenceRepository
 */
class VerifyUserAuthenticatedUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val preferenceRepository: IPreferenceRepository
): BaseUseCase<Boolean>() {
    override suspend fun onExecuted(): Boolean =
        userRepository.isAuthenticated() &&
                preferenceRepository.getAuthUserUid().isNotBlank()
}