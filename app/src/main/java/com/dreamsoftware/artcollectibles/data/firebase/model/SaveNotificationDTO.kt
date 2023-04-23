package com.dreamsoftware.artcollectibles.data.firebase.model

import java.math.BigInteger

data class SaveNotificationDTO(
    val uid: String,
    val title: String,
    val description: String,
    val userUid: String,
    val tokenId: BigInteger
)
