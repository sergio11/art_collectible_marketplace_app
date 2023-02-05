package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketplaceStatisticsDTO
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class MarketplaceStatisticsMapper: IOneSideMapper<MarketplaceStatisticsDTO, MarketplaceStatistics> {

    override fun mapInToOut(input: MarketplaceStatisticsDTO): MarketplaceStatistics = with(input) {
        MarketplaceStatistics(
            countSoldMarketItems, countAvailableMarketItems, countCanceledMarketItems
        )
    }

    override fun mapInListToOutList(input: Iterable<MarketplaceStatisticsDTO>): Iterable<MarketplaceStatistics> =
        input.map(::mapInToOut)
}