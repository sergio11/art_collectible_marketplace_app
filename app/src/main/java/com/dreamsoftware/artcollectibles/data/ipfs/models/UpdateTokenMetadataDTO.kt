package com.dreamsoftware.artcollectibles.data.ipfs.models

data class UpdateTokenMetadataDTO(
    val cid: String,
    val name: String? = null,
    val description: String? = null,
)
