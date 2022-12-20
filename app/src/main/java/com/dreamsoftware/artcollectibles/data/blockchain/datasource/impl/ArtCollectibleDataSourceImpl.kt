package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import kotlinx.coroutines.flow.Flow

class ArtCollectibleDataSourceImpl: IArtCollectibleDataSource {

    override fun getTokensCreated(): Flow<ArtCollectibleBlockchainEntity> {
        TODO("Not yet implemented")
    }

    override fun getTokensOwned(): Flow<ArtCollectibleBlockchainEntity> {
        TODO("Not yet implemented")
    }
}