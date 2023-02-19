package com.dreamsoftware.artcollectibles.domain.models


class CreateArtCollectible(
    val royalty: Long,
    val metadata: CreateArtCollectibleMetadata
)

class CreateArtCollectibleMetadata(
    val name: String,
    val description: String? = null,
    val fileUri: String,
    val mediaType: String,
    val tags: List<String>
)