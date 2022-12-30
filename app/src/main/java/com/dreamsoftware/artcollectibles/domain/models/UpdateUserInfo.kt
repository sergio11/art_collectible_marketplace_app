package com.dreamsoftware.artcollectibles.domain.models

data class UpdateUserInfo(
    val uid: String,
    val name: String? = null,
    val info: String? = null,
    val contact: String? = null,
    val photoUrl: String? = null
)
