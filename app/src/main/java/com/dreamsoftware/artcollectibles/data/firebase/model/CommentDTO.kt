package com.dreamsoftware.artcollectibles.data.firebase.model

import java.math.BigInteger
import java.util.*


data class CommentDTO(
    val uid: String,
    val comment: String,
    val userUid: String,
    val tokenId: BigInteger,
    val createdAt: Date
)
