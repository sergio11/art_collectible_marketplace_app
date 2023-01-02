package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class CloseSessionUseCase(
    private val userRepository: IUserRepository
): BaseUseCase<Unit>() {
    override suspend fun onExecuted(): Unit {
        userRepository.closeSession()
    }
}