package com.dreamsoftware.artcollectibles.data.ipfs.models.response

import com.dreamsoftware.artcollectibles.data.ipfs.utils.TOKEN_AUTHOR_ADDRESS
import com.dreamsoftware.artcollectibles.data.ipfs.utils.TOKEN_DESCRIPTION_KEY
import com.dreamsoftware.artcollectibles.data.ipfs.utils.TOKEN_OWNER_ADDRESS
import com.squareup.moshi.Json
import java.math.BigInteger
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
    val metadata: FilePinnedMetadataDTO,
    val imageUrl: String = ""
)

data class FilePinnedMetadataDTO(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "keyvalues")
    val keyValues: Map<String, String>
) {
    val description: String
        get() = keyValues[TOKEN_DESCRIPTION_KEY].orEmpty()

    val ownerAddress: String
        get() = keyValues[TOKEN_OWNER_ADDRESS].orEmpty()

    val authorAddress: String
        get() = keyValues[TOKEN_AUTHOR_ADDRESS].orEmpty()
}
