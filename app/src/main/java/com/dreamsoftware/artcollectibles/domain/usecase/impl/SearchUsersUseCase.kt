package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class SearchUsersUseCase(
    private val userRepository: IUserRepository
): BaseUseCaseWithParams<SearchUsersUseCase.Params, Iterable<UserInfo>>() {

    override suspend fun onExecuted(params: Params): Iterable<UserInfo> =
        userRepository.search(term = params.term)

    data class Params(
        val term: String?
    )
}