package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class FetchMarketplaceStatisticsUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCase<MarketplaceStatistics>() {
    override suspend fun onExecuted(): MarketplaceStatistics =
        artMarketplaceRepository.fetchMarketplaceStatistics()
}