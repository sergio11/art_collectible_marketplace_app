package com.dreamsoftware.artcollectibles.data.firebase.model

data class SaveUserDTO(
    val uid: String,
    val name: String,
    val walletAddress: String,
    val professionalTitle: String? = null,
    val info: String? = null,
    val contact: String? = null,
    val photoUrl: String? = null,
    val birthdate: String? = null,
    val externalAuthProvider: String? = null,
    val location: String? = null
)
