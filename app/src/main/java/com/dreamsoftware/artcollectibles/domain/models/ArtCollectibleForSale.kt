package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class ArtCollectibleForSale(
    val marketItemId: BigInteger,
    val token: ArtCollectible,
    val seller: UserInfo,
    val owner: UserInfo,
    val price: BigInteger,
    val sold: Boolean,
    val canceled: Boolean
)
