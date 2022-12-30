package com.dreamsoftware.artcollectibles.domain.models

data class AuthUser(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String
)
