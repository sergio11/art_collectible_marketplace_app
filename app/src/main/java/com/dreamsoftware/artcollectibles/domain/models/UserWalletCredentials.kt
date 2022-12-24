package com.dreamsoftware.artcollectibles.domain.models

import java.math.BigInteger

data class UserWalletCredentials(
    val address: String,
    val privateKey: BigInteger,
    val publicKey: BigInteger
)