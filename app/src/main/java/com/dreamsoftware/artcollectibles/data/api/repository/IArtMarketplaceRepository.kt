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
     * Fetch non sold and non canceled market items by category
     * @param categoryUid
     */
    @Throws(FetchAvailableMarketItemsByCategoryException::class)
    suspend fun fetchAvailableMarketItemsByCategory(categoryUid: String): Iterable<ArtCollectibleForSale>

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
     * @param priceInEth
     */
    @Throws(PutItemForSaleException::class)
    suspend fun putItemForSale(tokenId: BigInteger, priceInEth: Float)

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
     */
    @Throws(BuyItemException::class)
    suspend fun buyItem(tokenId: BigInteger)

    /**
     * Get similar market items
     * @param tokenId
     * @param count
     */
    @Throws(GetSimilarMarketItemsException::class)
    suspend fun getSimilarMarketItems(tokenId: BigInteger, count: Int): Iterable<ArtCollectibleForSale>

    /**
     * Get similar author market items
     * @param tokenId
     * @param count
     */
    @Throws(GetSimilarAuthorMarketItemsException::class)
    suspend fun getSimilarAuthorMarketItems(tokenId: BigInteger, count: Int): Iterable<ArtCollectibleForSale>
}