package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetMoreLikedTokensUseCase(
    private val favoritesRepository: IFavoritesRepository
): BaseUseCase<Iterable<ArtCollectible>>() {

    private companion object {
        const val LIMIT: Long = 5
    }

    override suspend fun onExecuted(): Iterable<ArtCollectible> =
        favoritesRepository.getMoreLikedTokens(LIMIT)
}