package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigDecimal
import java.math.BigInteger

data class AccountBalanceDTO(
    val balanceInWei: BigInteger,
    val balanceInEth: BigDecimal,
    val maticPricesDTO: MarketPricesDTO? = null
)
