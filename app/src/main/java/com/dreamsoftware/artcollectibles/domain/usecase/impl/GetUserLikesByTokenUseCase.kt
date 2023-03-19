package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetUserLikesByTokenUseCase(
    private val favoritesRepository: IFavoritesRepository
): BaseUseCaseWithParams<GetUserLikesByTokenUseCase.Params, Iterable<UserInfo>>() {

    override suspend fun onExecuted(params: Params): Iterable<UserInfo> =
        favoritesRepository.getUserLikesByToken(params.tokenId)

    data class Params(
        val tokenId: String
    )
}