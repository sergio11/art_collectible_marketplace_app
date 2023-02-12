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
     * Allows you to retrieve the list of tokens created by current auth user
     */
    @Throws(GetTokensCreatedException::class)
    suspend fun getTokensCreated(): Iterable<ArtCollectible>

    /**
     * Retrieve token information by id
     */
    @Throws(GetTokenByIdException::class)
    suspend fun getTokenById(tokenId: BigInteger): ArtCollectible
}