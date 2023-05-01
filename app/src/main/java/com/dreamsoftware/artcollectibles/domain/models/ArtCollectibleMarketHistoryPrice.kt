package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger
import java.util.*

data class ArtCollectibleMarketHistoryPrice(
    val marketItemId: BigInteger,
    val token: ArtCollectible,
    val price: ArtCollectiblePrices,
    val date: Date
)
