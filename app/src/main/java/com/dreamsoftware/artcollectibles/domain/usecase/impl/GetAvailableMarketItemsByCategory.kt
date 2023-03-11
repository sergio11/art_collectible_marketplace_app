package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetAvailableMarketItemsByCategory(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<GetAvailableMarketItemsByCategory.Params, Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleForSale> {
        return artMarketplaceRepository.fetchSellingMarketItems()
    }

    data class Params(
        val categoryUid: String
    )
}