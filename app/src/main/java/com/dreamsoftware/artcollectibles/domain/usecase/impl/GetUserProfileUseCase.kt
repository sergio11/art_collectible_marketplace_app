package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetUserProfileUseCase(
    private val userRepository: IUserRepository
): BaseUseCase<UserInfo>() {

    override suspend fun onExecuted(): UserInfo =
        userRepository.get()
}