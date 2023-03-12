package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetMoreFollowedUsersUseCase(
    private val userRepository: IUserRepository
): BaseUseCase<Iterable<UserInfo>>() {

    private companion object {
        const val LIMIT: Long = 5
    }

    override suspend fun onExecuted(): Iterable<UserInfo> =
        userRepository.getMoreFollowedUsers(LIMIT)
}