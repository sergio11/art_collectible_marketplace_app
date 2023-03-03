package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetFollowersUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<GetFollowersUseCase.Params, Iterable<UserInfo>>() {

    override suspend fun onExecuted(params: Params) =
        userRepository.getFollowers(params.userUid)

    data class Params(
        val userUid: String
    )
}