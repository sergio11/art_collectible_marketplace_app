package com.dreamsoftware.artcollectibles.data.firebase.model

data class UserDTO(
    val uid: String,
    val name: String,
    val info: String? = null,
    val contact: String? = null,
    val walletAddress: String
)
