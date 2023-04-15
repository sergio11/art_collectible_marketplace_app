package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigDecimal
import java.math.BigInteger

data class AccountBalance(
    val balanceInWei: BigInteger,
    val balanceInEth: BigDecimal,
    val balanceInEUR: Double? = null,
    val balanceInUSD: Double? = null,
)
