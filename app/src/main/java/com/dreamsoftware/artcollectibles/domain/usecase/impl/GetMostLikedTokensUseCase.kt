package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetMostLikedTokensUseCase(
    private val favoritesRepository: IFavoritesRepository
) : BaseUseCaseWithParams<GetMostLikedTokensUseCase.Params, Iterable<ArtCollectible>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectible> = with(params) {
        favoritesRepository.getMostLikedTokens(limit)
    }

    data class Params(
        val limit: Int
    )

}