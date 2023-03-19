package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger
import java.util.*

data class Comment(
    val uid: String,
    val text: String,
    val user: UserInfo,
    val tokenId: BigInteger,
    val createdAt: Date
)
