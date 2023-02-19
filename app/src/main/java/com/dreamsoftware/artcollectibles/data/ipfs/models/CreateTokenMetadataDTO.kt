package com.dreamsoftware.artcollectibles.data.ipfs.models


data class CreateTokenMetadataDTO(
    val name: String,
    val description: String? = null,
    val fileUri: String,
    val mediaType: String,
    val tags: List<String>
)
