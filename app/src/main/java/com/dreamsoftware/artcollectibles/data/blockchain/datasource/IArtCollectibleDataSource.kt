package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import kotlinx.coroutines.flow.Flow

interface IArtCollectibleDataSource {
    suspend fun getTokensCreated(): Flow<ArtCollectibleBlockchainEntity>
    suspend fun getTokensOwned(): Flow<ArtCollectibleBlockchainEntity>
}