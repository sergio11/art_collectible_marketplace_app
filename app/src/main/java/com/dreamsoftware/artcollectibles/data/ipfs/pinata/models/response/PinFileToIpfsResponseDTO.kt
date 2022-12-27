package com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.response

import com.squareup.moshi.Json
import java.util.Date

data class PinFileToIpfsResponseDTO(
    @field:Json(name = "IpfsHash")
    val ipfsHash: String,
    @field:Json(name = "PinSize")
    val pinSize: Long,
    @field:Json(name = "Timestamp")
    val timestamp: Date
)
