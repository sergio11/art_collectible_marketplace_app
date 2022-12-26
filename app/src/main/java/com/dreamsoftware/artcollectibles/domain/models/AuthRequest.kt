package com.dreamsoftware.artcollectibles.domain.models

data class AuthRequest(
    val accessToken: String,
    val authTypeEnum: AuthTypeEnum
)
