package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleMarketHistoryPriceDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.util.*

class ArtCollectibleMarketPriceMapper(
    private val artCollectibleForSalePricesMapper: ArtCollectibleForSalePricesMapper
) : IOneSideMapper<ArtMarketplaceContract.ArtCollectibleMarketPrice, ArtCollectibleMarketHistoryPriceDTO> {

    override fun mapInToOut(input: ArtMarketplaceContract.ArtCollectibleMarketPrice): ArtCollectibleMarketHistoryPriceDTO = with(input) {
        ArtCollectibleMarketHistoryPriceDTO(
            marketItemId = marketItemId,
            tokenId = tokenId,
            prices = artCollectibleForSalePricesMapper.mapInToOut(price),
            date = Date(date.toLong() * 1000)
        )
    }

    override fun mapInListToOutList(input: Iterable<ArtMarketplaceContract.ArtCollectibleMarketPrice>): Iterable<ArtCollectibleMarketHistoryPriceDTO> =
        input.map(::mapInToOut)
}