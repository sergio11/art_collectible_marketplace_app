package com.dreamsoftware.artcollectibles.data.firebase.model

data class UserDTO(
    val uid: String,
    val name: String,
    val professionalTitle: String? = null,
    val info: String? = null,
    val contact: String? = null,
    val photoUrl: String? = null,
    val walletAddress: String,
    val birthdate: String? = null,
    val externalProviderAuth: String? = null,
    val location: String? = null
)
