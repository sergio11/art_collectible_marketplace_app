package com.dreamsoftware.artcollectibles.domain.models

data class UserInfo(
    val uid: String,
    val name: String,
    val info: String,
    val contact: String,
    val walletAddress: String
)
