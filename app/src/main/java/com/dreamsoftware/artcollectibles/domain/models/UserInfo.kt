package com.dreamsoftware.artcollectibles.domain.models

data class UserInfo(
    val uid: String,
    val name: String,
    val professionalTitle: String? = null,
    val info: String? = null,
    val contact: String,
    val photoUrl: String? = null,
    val walletAddress: String,
    val birthdate: String? = null,
    val tags: List<String>? = null,
    val externalProviderAuthType: ExternalProviderAuthTypeEnum? = null,
    val location: String? = null,
    val tokensSoldCount: Long = 0,
    val tokensBoughtCount: Long = 0,
    val tokensOwnedCount: Long = 0,
    val tokensCreatedCount: Long = 0,
    val followers: Long = 0,
    val following: Long = 0
)
