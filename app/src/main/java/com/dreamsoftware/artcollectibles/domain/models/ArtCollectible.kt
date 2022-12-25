package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class ArtCollectible(
    val id: BigInteger,
    val name: String,
    val imageUrl: String,
    val description: String,
    val royalty: BigInteger,
    val author: UserInfo
)