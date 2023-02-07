package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.ArtMarketplaceDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import java.math.BigInteger

interface IArtMarketplaceRepository {

    /**
     * Fetch non sold and non canceled market items
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are being listed by the current authenticated user
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are owned by the current authenticated user
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch market history
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSale>

    /**
     * Put item for Sale
     * @param tokenId
     * @param price
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun putItemForSale(tokenId: BigInteger, price: Float)

    /**
     * Withdraw from sale
     * @param tokenId
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun withdrawFromSale(tokenId: BigInteger)

    /**
     * Is Token Added for sale
     * @param tokenId
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean

    /**
     * Fetch Marketplace statistics
     */
    @Throws(ArtMarketplaceDataException::class)
    suspend fun fetchMarketplaceStatistics(): MarketplaceStatistics
}