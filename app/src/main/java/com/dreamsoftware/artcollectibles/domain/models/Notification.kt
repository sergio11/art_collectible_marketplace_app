package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger
import java.util.*

data class Notification(
    val uid: String,
    val title: String,
    val description: String,
    val user: UserInfo,
    val tokenId: BigInteger,
    val createdAt: Date
)
