package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

class ArtCollectible(
    val id: BigInteger,
    val name: String,
    val imageUrl: String,
    val description: String,
    val royalty: BigInteger,
    val author: UserInfo,
    val favoritesCount: Long
) {
    val displayName: String
        get() = "#$id - $name"
}