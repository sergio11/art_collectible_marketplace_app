package com.dreamsoftware.artcollectibles.data.api

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale

interface IArtMarketplaceRepository {

    /**
     * Fetch non sold and non canceled market items
     */
    suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are being listed by the current authenticated user
     */
    suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are owned by the current authenticated user
     */
    suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch market history
     */
    suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSale>
}