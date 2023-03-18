package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class Comment(
    val uid: String,
    val comment: String,
    val user: UserInfo,
    val tokenId: BigInteger
)