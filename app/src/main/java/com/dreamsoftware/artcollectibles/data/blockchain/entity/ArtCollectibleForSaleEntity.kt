package com.dreamsoftware.artcollectibles.data.blockchain.entity

import java.math.BigInteger

data class ArtCollectibleForSaleEntity(
    val marketItemId: BigInteger,
    val tokenId: BigInteger,
    val creator: String,
    val seller: String,
    val owner: String,
    val price: BigInteger,
    val sold: Boolean,
    val canceled: Boolean
)
