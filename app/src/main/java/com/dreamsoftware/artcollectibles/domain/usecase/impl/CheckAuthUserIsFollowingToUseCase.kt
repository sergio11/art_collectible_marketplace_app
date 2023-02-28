package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class CheckAuthUserIsFollowingToUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<CheckAuthUserIsFollowingToUseCase.Params, Boolean>() {

    override suspend fun onExecuted(params: Params): Boolean =
        userRepository.isFollowingTo(params.userUid)

    data class Params(
        val userUid: String
    )
}