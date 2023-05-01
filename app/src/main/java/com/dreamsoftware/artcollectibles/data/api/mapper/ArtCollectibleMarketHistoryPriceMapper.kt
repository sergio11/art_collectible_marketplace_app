package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleMarketHistoryPriceDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketHistoryPrice
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectiblePrices
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMarketHistoryPriceMapper: IOneSideMapper<ArtCollectibleMarketHistoryPriceMapper.InputData, ArtCollectibleMarketHistoryPrice> {

    override fun mapInToOut(input: InputData): ArtCollectibleMarketHistoryPrice = with(input) {
        with(artCollectibleMarketHistoryPriceDTO) {
            ArtCollectibleMarketHistoryPrice(
                marketItemId = artCollectibleMarketHistoryPriceDTO.marketItemId,
                token = artCollectible,
                price = ArtCollectiblePrices(
                    priceInEth = prices.priceInEth,
                    priceInWei = prices.priceInWei
                ),
                date = date
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<ArtCollectibleMarketHistoryPrice> =
        input.map(::mapInToOut)

    data class InputData(
        val artCollectible: ArtCollectible,
        val artCollectibleMarketHistoryPriceDTO: ArtCollectibleMarketHistoryPriceDTO
    )
}