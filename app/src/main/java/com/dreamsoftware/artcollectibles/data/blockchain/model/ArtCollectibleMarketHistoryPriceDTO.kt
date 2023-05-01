package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger
import java.util.Date

data class ArtCollectibleMarketHistoryPriceDTO(
    val marketItemId: BigInteger,
    val tokenId: BigInteger,
    val prices: ArtCollectibleForSalePricesDTO,
    val date: Date
)
