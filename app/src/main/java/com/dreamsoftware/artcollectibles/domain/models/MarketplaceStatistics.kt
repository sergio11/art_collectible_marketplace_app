package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class MarketplaceStatistics(
    val countSoldMarketItems: BigInteger,
    val countAvailableMarketItems: BigInteger,
    val countCanceledMarketItems: BigInteger
)
