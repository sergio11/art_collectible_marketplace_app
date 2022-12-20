package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import kotlinx.coroutines.flow.Flow

interface IArtCollectibleDataSource {
    fun getTokensCreated(): Flow<ArtCollectibleBlockchainEntity>
    fun getTokensOwned(): Flow<ArtCollectibleBlockchainEntity>
}