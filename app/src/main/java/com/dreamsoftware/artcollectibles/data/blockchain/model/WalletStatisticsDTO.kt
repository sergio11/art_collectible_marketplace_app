package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger

data class WalletStatisticsDTO(
    val countTokenSold: BigInteger,
    val countTokenBought: BigInteger,
    val countTokenWithdrawn: BigInteger
)
