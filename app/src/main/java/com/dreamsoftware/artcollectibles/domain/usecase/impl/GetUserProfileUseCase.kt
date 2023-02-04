package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetUserProfileUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<GetUserProfileUseCase.Params, UserInfo>() {

    override suspend fun onExecuted(params: Params): UserInfo =
        userRepository.get(uid = params.uid)

    data class Params(
        val uid: String
    )
}