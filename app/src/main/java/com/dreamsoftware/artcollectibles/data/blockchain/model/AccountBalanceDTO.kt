package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigDecimal
import java.math.BigInteger

data class AccountBalanceDTO(
    val erc20: BigInteger,
    val balanceInWei: BigInteger,
    val balanceInEth: BigDecimal
)
