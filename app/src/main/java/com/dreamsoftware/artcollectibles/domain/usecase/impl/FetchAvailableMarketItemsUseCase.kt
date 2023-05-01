package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchAvailableMarketItemsUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<FetchAvailableMarketItemsUseCase.Params, Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleForSale> = with(params) {
        artMarketplaceRepository.fetchAvailableMarketItems(limit)
    }

    data class Params(
        val limit: Int? = null
    )
}