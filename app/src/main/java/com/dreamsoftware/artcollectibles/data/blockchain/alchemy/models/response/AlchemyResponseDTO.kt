package com.dreamsoftware.artcollectibles.data.blockchain.alchemy.models.response

import com.squareup.moshi.Json

data class AlchemyResponseDTO(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "jsonrpc")
    val jsonRpc: String,
    @field:Json(name = "result")
    val result: String
)
