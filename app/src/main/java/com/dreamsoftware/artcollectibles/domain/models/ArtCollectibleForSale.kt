package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger
import java.util.*

data class ArtCollectibleForSale(
    val marketItemId: BigInteger,
    val token: ArtCollectible,
    val seller: UserInfo,
    val owner: UserInfo? = null,
    val price: BigInteger,
    val sold: Boolean,
    val canceled: Boolean,
    val putForSaleAt: Date,
    val soldAt: Date? = null,
    val canceledAt: Date? = null
)
