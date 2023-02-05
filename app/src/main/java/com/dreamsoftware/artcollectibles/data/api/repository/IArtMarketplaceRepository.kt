package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import java.math.BigInteger

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

    /**
     * Put item for Sale
     * @param tokenId
     * @param price
     */
    suspend fun putItemForSale(tokenId: BigInteger, price: Float)

    /**
     * Withdraw from sale
     * @param tokenId
     */
    suspend fun withdrawFromSale(tokenId: BigInteger)

    /**
     * Is Token Added for sale
     * @param tokenId
     */
    suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean

    /**
     * Fetch Marketplace statistics
     */
    suspend fun fetchMarketplaceStatistics(): MarketplaceStatistics
}