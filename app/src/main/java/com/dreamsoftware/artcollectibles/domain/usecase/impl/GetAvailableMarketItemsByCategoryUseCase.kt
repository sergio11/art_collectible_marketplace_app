package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetAvailableMarketItemsByCategoryUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<GetAvailableMarketItemsByCategoryUseCase.Params, Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleForSale> {
        return artMarketplaceRepository.fetchAvailableMarketItemsByCategory(categoryUid = params.categoryUid)
    }

    data class Params(
        val categoryUid: String
    )
}