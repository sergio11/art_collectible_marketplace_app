package com.dreamsoftware.artcollectibles.domain.models


class CreateArtCollectible(
    val name: String,
    val description: String? = null,
    val royalty: Long,
    val fileUri: String,
    val mediaType: String,
    val tags: List<String>
)