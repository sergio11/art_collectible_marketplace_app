package com.dreamsoftware.artcollectibles.data.firebase.model

import java.math.BigInteger


data class CommentDTO(
    val uid: String,
    val comment: String,
    val userUid: String,
    val tokenId: BigInteger
)
