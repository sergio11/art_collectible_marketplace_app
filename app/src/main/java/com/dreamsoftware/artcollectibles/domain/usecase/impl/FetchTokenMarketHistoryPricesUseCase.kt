package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketHistoryPrice
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class FetchTokenMarketHistoryPricesUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<FetchTokenMarketHistoryPricesUseCase.Params, Iterable<ArtCollectibleMarketHistoryPrice>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleMarketHistoryPrice> =
        artMarketplaceRepository.fetchTokenMarketHistoryPrices(params.tokenId)

    data class Params(
        val tokenId: BigInteger
    )
}