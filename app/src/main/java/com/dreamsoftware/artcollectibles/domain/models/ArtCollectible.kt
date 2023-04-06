package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger
import java.util.*

data class ArtCollectible(
    val id: BigInteger,
    val royalty: BigInteger,
    val metadata: ArtCollectibleMetadata,
    val favoritesCount: Long,
    val hasAddedToFav: Boolean,
    val visitorsCount: Long,
    val commentsCount: Long,
    val author: UserInfo,
    val owner: UserInfo,
) {
    val displayName: String
        get() = "#$id - ${metadata.name}"
}

data class ArtCollectibleMetadata(
    val cid: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val tags: List<String>,
    val createdAt: Date,
    val category: ArtCollectibleCategory,
    val authorAddress: String,
    val deviceName: String
)