package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import java.math.BigInteger

interface IArtCollectibleBlockchainDataSource {

    /**
     * Allow us to mint a new token
     */
    suspend fun mintToken(metadataCid: String, royalty: Long): BigInteger

    /**
     * Allows you to retrieve the list of tokens created
     */
    suspend fun getTokensCreated(): Iterable<ArtCollectibleBlockchainEntity>

    /**
     * Allows you to retrieve the list of tokens owned
     */
    suspend fun getTokensOwned(): Iterable<ArtCollectibleBlockchainEntity>

    /**
     * Retrieve token information by id
     */
    suspend fun getTokenById(tokenId: BigInteger): ArtCollectibleBlockchainEntity
}