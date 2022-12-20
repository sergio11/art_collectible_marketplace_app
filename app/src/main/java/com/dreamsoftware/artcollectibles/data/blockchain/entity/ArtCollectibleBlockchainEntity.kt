package com.dreamsoftware.artcollectibles.data.blockchain.entity

import java.math.BigInteger

data class ArtCollectibleBlockchainEntity(
    val creator: String,
    val royalty: BigInteger,
    val isExist: Boolean
)
