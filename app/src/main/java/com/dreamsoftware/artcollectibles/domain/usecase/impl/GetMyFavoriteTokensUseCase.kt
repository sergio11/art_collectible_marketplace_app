package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetMyFavoriteTokensUseCase(
    private val favoritesRepository: IFavoritesRepository
): BaseUseCase<Iterable<ArtCollectible>>() {

    override suspend fun onExecuted(): Iterable<ArtCollectible> =
        favoritesRepository.getMyFavoriteTokens()
}