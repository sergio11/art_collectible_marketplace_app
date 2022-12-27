package com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.request

import com.squareup.moshi.Json

data class UpdateFileMetadataDTO(
    @field:Json(name = "ipfsPinHash")
    val cid: String,
    @field:Json(name = "name")
    val name: String? = null,
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
)
