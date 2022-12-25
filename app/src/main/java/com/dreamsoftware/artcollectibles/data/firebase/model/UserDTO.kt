package com.dreamsoftware.artcollectibles.data.firebase.model

data class UserDTO(
    val uid: String,
    val name: String,
    val info: String,
    val contact: String,
    val walletAddress: String
)
