package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchMarketHistoryUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<FetchMarketHistoryUseCase.Params, Iterable<ArtCollectibleForSale>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleForSale> = with(params) {
        artMarketplaceRepository.fetchMarketHistory()
    }

    data class Params(
        val limit: Int? = null
    )
}