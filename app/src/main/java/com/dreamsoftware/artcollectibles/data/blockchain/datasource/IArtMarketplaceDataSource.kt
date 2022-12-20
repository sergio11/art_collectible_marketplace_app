package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import kotlinx.coroutines.flow.Flow

interface IArtMarketplaceDataSource {

    /**
     * Fetch non sold and non canceled market items
     */
    suspend fun fetchAvailableMarketItems(): Flow<ArtCollectibleForSaleEntity>

    /**
     * Fetch market items that are being listed by the current authenticated user
     */
    suspend fun fetchSellingMarketItems(): Flow<ArtCollectibleForSaleEntity>

    /**
     * Fetch market items that are owned by the current authenticated user
     */
    suspend fun fetchOwnedMarketItems(): Flow<ArtCollectibleForSaleEntity>

}