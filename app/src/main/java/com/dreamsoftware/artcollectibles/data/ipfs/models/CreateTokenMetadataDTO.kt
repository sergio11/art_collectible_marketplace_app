package com.dreamsoftware.artcollectibles.data.ipfs.models

import java.io.File

data class CreateTokenMetadataDTO(
    val name: String,
    val description: String? = null,
    val fileUri: String,
    val mediaType: String,
    val authorAddress: String
)
