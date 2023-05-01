package com.dreamsoftware.artcollectibles.data.blockchain.mapper

import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSalePricesDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.math.BigDecimal
import java.math.BigInteger

class ArtCollectibleForSalePricesMapper: IOneSideMapper<BigInteger, ArtCollectibleForSalePricesDTO> {

    override fun mapInToOut(input: BigInteger): ArtCollectibleForSalePricesDTO =
        ArtCollectibleForSalePricesDTO(
            priceInWei = input,
            priceInEth = input.toBigDecimal().divide(BigDecimal.valueOf(1000000000000000000L))
        )

    override fun mapInListToOutList(input: Iterable<BigInteger>): Iterable<ArtCollectibleForSalePricesDTO> =
        input.map(::mapInToOut)
}