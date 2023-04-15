package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class GetSimilarAuthorMarketItemsUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<GetSimilarAuthorMarketItemsUseCase.Params, Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleForSale> = with(params) {
        artMarketplaceRepository.getSimilarAuthorMarketItems(tokenId, count)
    }

    data class Params(
        val tokenId: BigInteger,
        val count: Int
    )
}