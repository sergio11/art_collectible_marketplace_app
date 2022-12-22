package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import kotlinx.coroutines.flow.Flow

class ArtCollectibleRepositoryImpl(
    private val artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
    private val ipfsDataSource: IpfsDataSource
): IArtCollectibleRepository {


    override suspend fun getTokensOwned(): Flow<ArtCollectible> {
       TODO("Implement")
    }
}