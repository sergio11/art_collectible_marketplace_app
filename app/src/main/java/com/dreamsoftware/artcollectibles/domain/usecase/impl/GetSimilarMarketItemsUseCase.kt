package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class GetSimilarMarketItemsUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<GetSimilarMarketItemsUseCase.Params, Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleForSale> = with(params) {
        artMarketplaceRepository.getSimilarMarketItems(tokenId, count)
    }

    data class Params(
        val tokenId: BigInteger,
        val count: Int
    )
}