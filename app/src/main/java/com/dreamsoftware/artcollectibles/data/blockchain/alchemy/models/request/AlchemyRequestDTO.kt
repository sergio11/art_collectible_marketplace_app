package com.dreamsoftware.artcollectibles.data.blockchain.alchemy.models.request

import com.squareup.moshi.Json

data class AlchemyRequestDTO(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonRpc: String,
    @field:Json(name = "params")
    val params: List<String>,
    @field:Json(name = "method")
    val method: String
)
