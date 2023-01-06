package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigDecimal
import java.math.BigInteger

data class AccountBalance(
    val erc20: BigInteger,
    val balanceInWei: BigInteger,
    val balanceInEth: BigDecimal
)
