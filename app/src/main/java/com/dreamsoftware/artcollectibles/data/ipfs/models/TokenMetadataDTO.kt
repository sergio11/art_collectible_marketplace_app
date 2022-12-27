package com.dreamsoftware.artcollectibles.data.ipfs.models

import java.util.Date

data class TokenMetadataDTO(
    val cid: String,
    val name: String,
    val description: String? = null,
    val createdAt: Date,
    val imageUrl: String,
    val authorAddress: String,
    val ownerAddress: String
)
