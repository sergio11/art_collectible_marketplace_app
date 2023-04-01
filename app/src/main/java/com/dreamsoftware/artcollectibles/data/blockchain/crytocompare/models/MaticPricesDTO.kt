package com.dreamsoftware.artcollectibles.data.blockchain.crytocompare.models

import com.squareup.moshi.Json

data class MaticPricesDTO(
    @field:Json(name = "USD")
    val priceUSD: Double,
    @field:Json(name = "EUR")
    val priceEUR: Double
)
