package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.*
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleMintedEventDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.TokenStatisticsDTO
import kotlinx.coroutines.flow.Flow
import org.web3j.crypto.Credentials
import java.math.BigInteger

interface IArtCollectibleBlockchainDataSource {

    /**
     * Observe Art Collectible Minted events
     */
    suspend fun observeArtCollectibleMintedEvents(credentials: Credentials): Flow<ArtCollectibleMintedEventDTO>

    /**
     * Allow us to mint a new token
     */
    @Throws(MintTokenException::class)
    suspend fun mintToken(metadataCid: String, royalty: Long, credentials: Credentials): BigInteger

    /**
     * Allow us to burn a token
     */
    @Throws(BurnTokenException::class)
    suspend fun burnToken(tokenId: BigInteger, credentials: Credentials)

    /**
     * Allows you to retrieve the list of tokens created
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreated(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens created by the creator address
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreatedBy(credentials: Credentials, creatorAddress: String): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens created by the creator address
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreatedBy(credentials: Credentials, creatorAddress: String, limit: Long): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens owned
     */
    @Throws(GetTokensOwnedException::class)
    suspend fun getTokensOwned(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens owned by the owner address
     */
    @Throws(GetTokensOwnedException::class)
    suspend fun getTokensOwnedBy(credentials: Credentials, ownerAddress: String): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens owned by the owner address
     */
    @Throws(GetTokensOwnedException::class)
    suspend fun getTokensOwnedBy(credentials: Credentials, ownerAddress: String, limit: Long): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Retrieve token information by id
     */
    @Throws(GetTokenByIdException::class)
    suspend fun getTokenById(tokenId: BigInteger, credentials: Credentials): ArtCollectibleBlockchainDTO

    /**
     * Retrieve token information by CID
     */
    @Throws(GetTokenByCidException::class)
    suspend fun getTokenByCID(cid: String, credentials: Credentials): ArtCollectibleBlockchainDTO

    /**
     * Retrieve a token list
     */
    @Throws(GetTokenByIdException::class)
    suspend fun getTokens(tokenList: Iterable<BigInteger>, credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Retrieve a token list
     */
    @Throws(GetTokenByCidException::class)
    suspend fun getTokensByCID(cidList: Iterable<String>, credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Fetch Tokens Statistics
     * @param credentials
     * @param targetAddress
     */
    @Throws(FetchTokensStatisticsException::class)
    suspend fun fetchTokensStatisticsByAddress(credentials: Credentials, targetAddress: String): TokenStatisticsDTO
}