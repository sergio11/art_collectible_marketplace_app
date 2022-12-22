package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import kotlinx.coroutines.flow.Flow

interface IArtCollectibleRepository {

    suspend fun getTokensOwned(): Flow<ArtCollectible>
}