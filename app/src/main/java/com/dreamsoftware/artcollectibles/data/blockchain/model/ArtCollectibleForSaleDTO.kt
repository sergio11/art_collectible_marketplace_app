package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger

data class ArtCollectibleForSaleDTO(
    val marketItemId: BigInteger,
    val tokenId: BigInteger,
    val creator: String,
    val seller: String,
    val owner: String,
    val price: BigInteger,
    val sold: Boolean,
    val canceled: Boolean
)
