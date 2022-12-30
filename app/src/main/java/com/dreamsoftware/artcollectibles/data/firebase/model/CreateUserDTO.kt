package com.dreamsoftware.artcollectibles.data.firebase.model

data class CreateUserDTO(
    val uid: String,
    val name: String,
    val contact: String? = null,
    val walletAddress: String,
    val photoUrl: String? = null
)
