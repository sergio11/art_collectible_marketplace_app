package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import kotlinx.coroutines.flow.Flow

interface IArtCollectibleRepository {

    /**
     * Allows you to retrieve the list of tokens owned
     */
    suspend fun getTokensOwned(): Flow<ArtCollectible>

    /**
     * Allows you to retrieve the list of tokens created
     */
    suspend fun getTokensCreated(): Flow<ArtCollectible>
}