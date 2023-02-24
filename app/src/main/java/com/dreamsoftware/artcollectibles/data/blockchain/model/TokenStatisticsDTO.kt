package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger

data class TokenStatisticsDTO(
    val countTokensCreator: BigInteger,
    val countTokensOwned: BigInteger
)
