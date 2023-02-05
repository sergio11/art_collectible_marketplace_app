package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger

data class MarketplaceStatisticsDTO(
    val countSoldMarketItems: BigInteger,
    val countAvailableMarketItems: BigInteger,
    val countCanceledMarketItems: BigInteger
)
