package com.dreamsoftware.artcollectibles.domain.models

data class Secret(
    val userUid: String,
    val secret: String,
    val salt: String
)
