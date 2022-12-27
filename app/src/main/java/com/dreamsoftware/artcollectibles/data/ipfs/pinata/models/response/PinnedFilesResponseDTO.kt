package com.dreamsoftware.artcollectibles.data.ipfs.pinata.models.response

import com.squareup.moshi.Json

data class PinnedFilesResponseDTO(
    @field:Json(name = "count")
    val count: Int,
    @field:Json(name = "rows")
    val rows: List<FilePinnedDTO>
)