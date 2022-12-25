package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import java.math.BigInteger

interface IArtCollectibleRepository {

    /**
     * Allows you to retrieve the list of tokens owned by current auth user
     */
    suspend fun getTokensOwned(): Iterable<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created by current auth user
     */
    suspend fun getTokensCreated(): Iterable<ArtCollectible>

    /**
     * Retrieve token information by id
     */
    suspend fun getTokenById(tokenId: BigInteger): ArtCollectible
}