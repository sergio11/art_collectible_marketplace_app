package com.dreamsoftware.artcollectibles.data.firebase.model

import java.math.BigInteger
import java.util.*


data class NotificationDTO(
    val uid: String,
    val title: String,
    val description: String,
    val userUid: String,
    val tokenId: BigInteger,
    val createdAt: Date
)
