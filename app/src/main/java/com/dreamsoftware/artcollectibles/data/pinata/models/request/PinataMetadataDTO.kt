package com.dreamsoftware.artcollectibles.data.pinata.models.request

import com.squareup.moshi.Json

/**
 * { "name": "MyFile",  "keyvalues": { "company": "Pinata" } }
 */
data class PinataMetadataDTO(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
)
