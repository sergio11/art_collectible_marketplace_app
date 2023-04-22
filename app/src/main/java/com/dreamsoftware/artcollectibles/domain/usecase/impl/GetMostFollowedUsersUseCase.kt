package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetMostFollowedUsersUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<GetMostFollowedUsersUseCase.Params, Iterable<UserInfo>>() {

    override suspend fun onExecuted(params: Params): Iterable<UserInfo> = with(params) {
        userRepository.getMostFollowedUsers(limit)
    }

    data class Params(
        val limit: Int
    )
}