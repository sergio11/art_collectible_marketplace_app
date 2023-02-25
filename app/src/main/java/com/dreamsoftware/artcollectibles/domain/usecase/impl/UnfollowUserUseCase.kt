package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class UnfollowUserUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<UnfollowUserUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            userRepository.unfollowUser(userUid)
        }
    }

    data class Params(
        val userUid: String
    )
}