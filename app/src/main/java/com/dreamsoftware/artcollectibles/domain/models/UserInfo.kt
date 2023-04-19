package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

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
    val country: String? = null,
    val instagramNick: String? = null,
    val tokensSoldCount: BigInteger = BigInteger.ZERO,
    val tokensBoughtCount: BigInteger = BigInteger.ZERO,
    val tokensOwnedCount: BigInteger = BigInteger.ZERO,
    val tokensCreatedCount: BigInteger = BigInteger.ZERO,
    val followers: Long = 0,
    val following: Long = 0
)
