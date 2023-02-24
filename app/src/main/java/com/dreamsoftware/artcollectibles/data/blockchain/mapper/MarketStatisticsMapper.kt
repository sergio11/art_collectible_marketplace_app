package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketplaceStatisticsDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class MarketStatisticsMapper : IOneSideMapper<ArtMarketplaceContract.MarketStatistics, MarketplaceStatisticsDTO> {

    override fun mapInToOut(input: ArtMarketplaceContract.MarketStatistics): MarketplaceStatisticsDTO =
        with(input) {
            MarketplaceStatisticsDTO(
                countAvailableMarketItems = countAvailable,
                countCanceledMarketItems = countCanceled,
                countSoldMarketItems = countSold
            )
        }

    override fun mapInListToOutList(input: Iterable<ArtMarketplaceContract.MarketStatistics>): Iterable<MarketplaceStatisticsDTO> =
        input.map(::mapInToOut)

}