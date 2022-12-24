package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class FetchSellingMarketItemsUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCase<Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(): Iterable<ArtCollectibleForSale> {
        TODO()
    }
}