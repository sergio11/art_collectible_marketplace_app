package com.dreamsoftware.artcollectibles.domain.models

data class CreateUserInfo(
    val uid: String,
    val name: String,
    val walletAddress: String,
    val contact: String? = null,
    val photoUrl: String? = null
)
