package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.ArtCollectibleDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectible
import java.math.BigInteger

interface IArtCollectibleRepository {

    /**
     * Allow us to save an art collectible token
     * @param token
     */
    @Throws(ArtCollectibleDataException::class)
    suspend fun create(token: CreateArtCollectible): ArtCollectible

    /**
     * Allow us to delete an art collectible token
     */
    @Throws(ArtCollectibleDataException::class)
    suspend fun delete(tokenId: BigInteger)

    /**
     * Allows you to retrieve the list of tokens owned by current auth user
     */
    @Throws(ArtCollectibleDataException::class)
    suspend fun getTokensOwned(): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by current auth user
     */
    @Throws(ArtCollectibleDataException::class)
    suspend fun getTokensCreated(): Iterable<ArtCollectible>

    /**
     * Retrieve token information by id
     */
    @Throws(ArtCollectibleDataException::class)
    suspend fun getTokenById(tokenId: BigInteger): ArtCollectible
}