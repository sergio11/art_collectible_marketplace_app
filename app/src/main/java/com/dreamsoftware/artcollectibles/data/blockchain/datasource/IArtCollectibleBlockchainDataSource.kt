package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import org.web3j.crypto.Credentials
import java.math.BigInteger

interface IArtCollectibleBlockchainDataSource {

    /**
     * Allow us to mint a new token
     */
    suspend fun mintToken(metadataCid: String, royalty: Long, credentials: Credentials): BigInteger

    /**
     * Allow us to burn a token
     */
    suspend fun burnToken(tokenId: BigInteger, credentials: Credentials)

    /**
     * Allows you to retrieve the list of tokens created
     */
    suspend fun getTokensCreated(credentials: Credentials): Iterable<ArtCollectibleBlockchainEntity>

    /**
     * Allows you to retrieve the list of tokens owned
     */
    suspend fun getTokensOwned(credentials: Credentials): Iterable<ArtCollectibleBlockchainEntity>

    /**
     * Retrieve token information by id
     */
    suspend fun getTokenById(tokenId: BigInteger, credentials: Credentials): ArtCollectibleBlockchainEntity
}