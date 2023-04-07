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
    @Throws(CreateArtCollectibleException::class)
    suspend fun create(token: CreateArtCollectible): ArtCollectible

    /**
     * Allow us to delete an art collectible token
     */
    @Throws(DeleteArtCollectibleException::class)
    suspend fun delete(tokenId: BigInteger)

    /**
     * Allows you to retrieve the list of tokens owned by current auth user
     */
    @Throws(GetTokensOwnedException::class)
    suspend fun getTokensOwned(): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens owned by owner address
     * @param ownerAddress
     */
    @Throws(GetTokensOwnedException::class)
    suspend fun getTokensOwnedBy(ownerAddress: String): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens owned by owner address
     * @param ownerAddress
     * @param limit
     */
    @Throws(GetTokensOwnedException::class)
    suspend fun getTokensOwnedBy(ownerAddress: String, limit: Long): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by current auth user
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreated(): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by creator address
     * @param creatorAddress
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreatedBy(creatorAddress: String): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by creator address
     * @param creatorAddress
     * @param limit
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreatedBy(creatorAddress: String, limit: Long): Iterable<ArtCollectible>

    /**
     * Retrieve token information by id
     */
    @Throws(GetTokenByIdException::class)
    suspend fun getTokenById(tokenId: BigInteger): ArtCollectible

    /**
     * Retrieve token list
     */
    @Throws(GetTokensException::class)
    suspend fun getTokens(tokenList: Iterable<BigInteger>): Iterable<ArtCollectible>

    /**
     * Get tokens by category
     */
    @Throws(GetTokensByCategoryException::class)
    suspend fun getTokensByCategory(categoryUid: String): Iterable<ArtCollectible>

    /**
     * Get Similar tokens
     */
    @Throws(GetTokensByCategoryException::class)
    suspend fun getSimilarTokens(tokenCid: String, count: Int): Iterable<ArtCollectible>
}