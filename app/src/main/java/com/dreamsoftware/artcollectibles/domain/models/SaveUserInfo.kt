package com.dreamsoftware.artcollectibles.domain.models

data class SaveUserInfo(
    val uid: String,
    val name: String,
    val walletAddress: String,
    val info: String? = null,
    val contact: String? = null,
    val photoUrl: String? = null
)
