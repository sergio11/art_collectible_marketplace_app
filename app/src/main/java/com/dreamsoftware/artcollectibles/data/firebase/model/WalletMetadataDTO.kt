package com.dreamsoftware.artcollectibles.data.firebase.model

data class WalletMetadataDTO(
    val userUid: String,
    val name: String,
    val secret: String,
    val walletUri: String
)
