package com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request

import com.squareup.moshi.Json

data class UpdateFileMetadataDTO(
    // CID for file where you want to update metadata
    @field:Json(name = "ipfsPinHash")
    val cid: String,
    // Name for the file
    @field:Json(name = "name")
    val name: String? = null,
    // Stringified object of key value pairs
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
)
