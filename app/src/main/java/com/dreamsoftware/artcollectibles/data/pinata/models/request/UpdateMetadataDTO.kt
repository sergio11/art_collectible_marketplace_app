package com.dreamsoftware.artcollectibles.data.pinata.models.request

import com.squareup.moshi.Json

data class UpdateMetadataDTO(
    @field:Json(name = "ipfsPinHash")
    val cid: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
)
