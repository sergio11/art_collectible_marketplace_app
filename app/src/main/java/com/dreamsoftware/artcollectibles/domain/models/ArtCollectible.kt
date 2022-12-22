package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class ArtCollectible(
    val id: BigInteger,
    val name: String,
    val imageUrl: String,
    val description: String,
    val author: AuthorInfo
)

data class AuthorInfo(
    val fullName: String,
    val contact: String,
    val royalty: BigInteger,
    val address: String
)