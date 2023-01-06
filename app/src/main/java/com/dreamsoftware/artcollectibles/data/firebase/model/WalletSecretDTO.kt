package com.dreamsoftware.artcollectibles.data.firebase.model

data class WalletSecretDTO(
    val userUid: String,
    val name: String,
    val secret: String,
    val walletUri: String
)
