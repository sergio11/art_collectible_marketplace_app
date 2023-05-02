package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.MarketStatisticDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketStatistic
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleMarketStatisticMapper: IOneSideMapper<ArtCollectibleMarketStatisticMapper.InputData, ArtCollectibleMarketStatistic> {

    override fun mapInToOut(input: InputData): ArtCollectibleMarketStatistic = with(input) {
        ArtCollectibleMarketStatistic(
            key = marketStatisticDTO.key.toBigInteger(),
            value = marketStatisticDTO.value,
            artCollectible = artCollectible
        )
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<ArtCollectibleMarketStatistic> =
        input.map(::mapInToOut)

    data class InputData(
        val artCollectible: ArtCollectible,
        val marketStatisticDTO: MarketStatisticDTO
    )
}