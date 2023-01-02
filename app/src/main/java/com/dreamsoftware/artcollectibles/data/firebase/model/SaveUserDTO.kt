package com.dreamsoftware.artcollectibles.data.firebase.model

data class SaveUserDTO(
    val uid: String,
    val name: String,
    val walletAddress: String,
    val info: String? = null,
    val contact: String? = null,
    val photoUrl: String? = null
)