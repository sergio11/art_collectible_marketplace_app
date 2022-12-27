package com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request

import com.squareup.moshi.Json

/**
 * { "name": "MyFile",  "keyvalues": { "company": "Pinata" } }
 */
data class FileMetadataDTO(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
)
