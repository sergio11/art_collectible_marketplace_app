package com.dreamsoftware.artcollectibles.data.pinata.models.response

import com.squareup.moshi.Json
import java.util.Date

data class FilePinnedDTO(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "ipfs_pin_hash")
    val ipfsPinHash: String,
    @field:Json(name = "size")
    val size: Long,
    @field:Json(name = "user_id")
    val userId: String,
    @field:Json(name = "date_pinned")
    val datePinned: Date,
    @field:Json(name = "metadata")
    val metadata: FilePinnedMetadataDTO
)

data class FilePinnedMetadataDTO(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
)
