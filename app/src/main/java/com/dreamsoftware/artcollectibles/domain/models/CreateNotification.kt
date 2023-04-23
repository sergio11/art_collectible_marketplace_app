package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class CreateNotification(
    val uid: String,
    val title: String,
    val description: String,
    val userUid: String,
    val tokenId: BigInteger
)
