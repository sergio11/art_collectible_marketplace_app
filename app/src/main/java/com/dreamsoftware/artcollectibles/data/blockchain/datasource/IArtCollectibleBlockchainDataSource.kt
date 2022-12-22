package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

interface IArtCollectibleBlockchainDataSource {

    /**
     * Allow us to mint a new token
     */
    suspend fun mintToken(metadataCid: String, royalty: Long): Flow<BigInteger>

    /**
     * Allows you to retrieve the list of tokens created
     */
    suspend fun getTokensCreated(): Flow<ArtCollectibleBlockchainEntity>

    /**
     * Allows you to retrieve the list of tokens owned
     */
    suspend fun getTokensOwned(): Flow<ArtCollectibleBlockchainEntity>
}