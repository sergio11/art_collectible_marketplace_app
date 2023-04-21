package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

data class ArtCollectibleForSaleDTO(
    val marketItemId: BigInteger,
    val tokenId: BigInteger,
    val metadataCID: String,
    val creator: String,
    val seller: String,
    val owner: String,
    val prices: ArtCollectibleForSalePricesDTO,
    val sold: Boolean,
    val canceled: Boolean,
    val putForSaleAt: Date,
    val soldAt: Date?,
    val canceledAt: Date?
)

data class ArtCollectibleForSalePricesDTO(
    val priceInWei: BigInteger,
    val priceInEth: BigDecimal
)