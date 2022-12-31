package com.dreamsoftware.artcollectibles.domain.models

data class ExternalProviderAuthRequest(
    val accessToken: String,
    val externalAuthTypeEnum: ExternalAuthTypeEnum
)
