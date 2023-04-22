package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class FetchMarketHistoryItemUseCase(
    private val artCollectibleMarketplaceRepository: IArtMarketplaceRepository
) : BaseUseCaseWithParams<FetchMarketHistoryItemUseCase.Params, ArtCollectibleForSale>() {

    override suspend fun onExecuted(params: Params): ArtCollectibleForSale =
        artCollectibleMarketplaceRepository.fetchMarketHistoryItem(marketItemId = params.marketItemId)

    data class Params(
        val marketItemId: BigInteger
    )
}