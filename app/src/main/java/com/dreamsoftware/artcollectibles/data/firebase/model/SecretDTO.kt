package com.dreamsoftware.artcollectibles.data.firebase.model

data class SecretDTO(
    val userUid: String,
    val secret: String,
    val salt: String
)
