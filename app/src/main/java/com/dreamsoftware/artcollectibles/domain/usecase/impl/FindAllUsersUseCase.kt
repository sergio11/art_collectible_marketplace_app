package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class FindAllUsersUseCase(
    private val userRepository: IUserRepository
): BaseUseCase<Iterable<UserInfo>>() {

    override suspend fun onExecuted(): Iterable<UserInfo> =
        userRepository.findAll()
}