package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class ArtCollectibleMarketStatistic(
    val key: BigInteger,
    val value: Long,
    val artCollectible: ArtCollectible
)
