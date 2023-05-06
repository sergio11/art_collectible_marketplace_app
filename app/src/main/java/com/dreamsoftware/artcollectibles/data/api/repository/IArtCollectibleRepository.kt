package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMintedEvent
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectible
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger

interface IArtCollectibleRepository {

    /**
     * Observe Art Collectible Minted events
     */
    @Throws(ObserveArtCollectibleMintedEventsException::class)
    suspend fun observeArtCollectibleMintedEvents(): Flow<ArtCollectibleMintedEvent>

    /**
     * Allow us to save an art collectible token
     * @param token
     */
    @Throws(CreateArtCollectibleDataException::class)
    suspend fun create(token: CreateArtCollectible): ArtCollectible

    /**
     * Allow us to delete an art collectible token
     */
    @Throws(DeleteArtCollectibleDataException::class)
    suspend fun delete(tokenId: BigInteger)

    /**
     * Allows you to retrieve the list of tokens owned by current auth user
     */
    @Throws(GetTokensOwnedDataException::class)
    suspend fun getTokensOwned(): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens owned by owner address
     * @param ownerAddress
     */
    @Throws(GetTokensOwnedDataException::class)
    suspend fun getTokensOwnedBy(ownerAddress: String): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens owned by owner address
     * @param ownerAddress
     * @param limit
     */
    @Throws(GetTokensOwnedDataException::class)
    suspend fun getTokensOwnedBy(ownerAddress: String, limit: Long): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by current auth user
     */
    @Throws(GetTokensCreatedDataException::class)
    suspend fun getTokensCreated(): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by creator address
     * @param creatorAddress
     */
    @Throws(GetTokensCreatedDataException::class)
    suspend fun getTokensCreatedBy(creatorAddress: String): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by creator address
     * @param creatorAddress
     * @param limit
     */
    @Throws(GetTokensCreatedDataException::class)
    suspend fun getTokensCreatedBy(creatorAddress: String, limit: Long): Iterable<ArtCollectible>

    /**
     * Retrieve token information by id
     */
    @Throws(GetTokenByIdDataException::class)
    suspend fun getTokenById(tokenId: BigInteger): ArtCollectible

    /**
     * Retrieve token list
     */
    @Throws(GetTokensDataException::class)
    suspend fun getTokens(tokenList: Iterable<BigInteger>): Iterable<ArtCollectible>

    /**
     * Get tokens by category
     */
    @Throws(GetTokensByCategoryDataException::class)
    suspend fun getTokensByCategory(categoryUid: String): Iterable<ArtCollectible>

    /**
     * Get Similar tokens
     */
    @Throws(GetTokensByCategoryDataException::class)
    suspend fun getSimilarTokens(tokenCid: String, count: Int): Iterable<ArtCollectible>
}