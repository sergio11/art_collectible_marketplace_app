package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetFollowingUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<GetFollowingUseCase.Params, Iterable<UserInfo>>() {

    override suspend fun onExecuted(params: Params) =
        userRepository.getFollowing(params.userUid)

    data class Params(
        val userUid: String
    )
}