package com.dreamsoftware.artcollectibles.data.firebase.model

data class UpdateUserDTO(
    val uid: String,
    val name: String? = null,
    val info: String? = null,
    val contact: String? = null,
    val photoUrl: String? = null
)
