package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class CreateComment(
    val uid: String,
    val comment: String,
    val userUid: String,
    val tokenId: BigInteger
)
