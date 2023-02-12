package com.dreamsoftware.artcollectibles.data.blockchain.model

import java.math.BigInteger

data class ArtCollectibleMintedEventDTO(
    val tokenId: BigInteger,
    val creator: String,
    val metadata: String,
    val royalty: BigInteger
)
