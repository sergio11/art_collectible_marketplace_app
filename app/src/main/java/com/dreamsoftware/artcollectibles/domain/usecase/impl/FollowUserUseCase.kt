package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FollowUserUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<FollowUserUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            userRepository.followUser(userUid)
        }
    }

    data class Params(
        val userUid: String
    )
}