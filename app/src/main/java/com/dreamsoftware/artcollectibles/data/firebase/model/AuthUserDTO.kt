package com.dreamsoftware.artcollectibles.data.firebase.model

data class AuthUserDTO(
    val uid: String,
    val displayName: String? = null,
    val email: String? = null
)
