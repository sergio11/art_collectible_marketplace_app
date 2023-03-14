package com.dreamsoftware.artcollectibles.data.firebase.model

import java.math.BigInteger

data class SaveCommentDTO(
    val uid: String,
    val comment: String,
    val userUid: String,
    val tokenId: BigInteger
)
