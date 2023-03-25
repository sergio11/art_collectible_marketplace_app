package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import java.math.BigInteger

interface IArtMarketplaceRepository {

    /**
     * Fetch non sold and non canceled market items
     */
    @Throws(FetchAvailableMarketItemsException::class)
    suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are being listed by the current authenticated user
     */
    @Throws(FetchSellingMarketItemsException::class)
    suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are owned by the current authenticated user
     */
    @Throws(FetchOwnedMarketItemsException::class)
    suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch market history
     */
    @Throws(FetchMarketHistoryException::class)
    suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch token market history
     * @param tokenId
     */
    @Throws(FetchMarketHistoryException::class)
    suspend fun fetchTokenMarketHistory(tokenId: BigInteger): Iterable<ArtCollectibleForSale>

    /**
     * Put item for Sale
     * @param tokenId
     * @param price
     */
    @Throws(PutItemForSaleException::class)
    suspend fun putItemForSale(tokenId: BigInteger, price: Float)

    /**
     * Fetch item for Sale
     * @param tokenId
     */
    @Throws(FetchItemForSaleException::class)
    suspend fun fetchItemForSale(tokenId: BigInteger): ArtCollectibleForSale

    /**
     * Withdraw from sale
     * @param tokenId
     */
    @Throws(WithdrawFromSaleException::class)
    suspend fun withdrawFromSale(tokenId: BigInteger)

    /**
     * Is Token Added for sale
     * @param tokenId
     */
    @Throws(CheckTokenAddedForSaleException::class)
    suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean

    /**
     * Fetch Marketplace statistics
     */
    @Throws(FetchMarketplaceStatisticsException::class)
    suspend fun fetchMarketplaceStatistics(): MarketplaceStatistics

    /**
     * Buy item
     * @param tokenId
     * @param price
     */
    @Throws(BuyItemException::class)
    suspend fun buyItem(tokenId: BigInteger, price: BigInteger)
}