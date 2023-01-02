package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase
import javax.inject.Inject

class VerifyUserAuthenticatedUseCase @Inject constructor(
    private val userRepository: IUserRepository
): BaseUseCase<Boolean>() {
    override suspend fun onExecuted(): Boolean = userRepository.isAuthenticated()
}