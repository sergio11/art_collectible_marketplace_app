package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger

data class ArtCollectibleBlockchainDTO(
    val tokenId: BigInteger,
    val creator: String,
    val royalty: BigInteger,
    val metadataCID: String,
    val isExist: Boolean
)
