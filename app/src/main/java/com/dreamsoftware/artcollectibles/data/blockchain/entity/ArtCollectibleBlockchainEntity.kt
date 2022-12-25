package com.dreamsoftware.artcollectibles.data.blockchain.entity

import java.math.BigInteger

data class ArtCollectibleBlockchainEntity(
    val tokenId: BigInteger,
    val creator: String,
    val royalty: BigInteger,
    val metadataCID: String,
    val isExist: Boolean
)
