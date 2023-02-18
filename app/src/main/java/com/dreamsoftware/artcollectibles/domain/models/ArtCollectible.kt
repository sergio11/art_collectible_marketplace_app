package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger
import java.util.*

class ArtCollectible(
    val id: BigInteger,
    val name: String,
    val imageUrl: String,
    val description: String,
    val royalty: BigInteger,
    val author: UserInfo,
    val owner: UserInfo,
    val favoritesCount: Long,
    val hasAddedToFav: Boolean,
    val tags: List<String>,
    val createdAt: Date
) {
    val displayName: String
        get() = "#$id - $name"
}