package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketHistoryPrice
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectiblePrices
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import java.math.BigInteger

interface IArtMarketplaceRepository {

    /**
     * Fetch non sold and non canceled market items
     * @param limit
     */
    @Throws(FetchAvailableMarketItemsDataException::class)
    suspend fun fetchAvailableMarketItems(limit: Int? = null): Iterable<ArtCollectibleForSale>

    /**
     * Fetch non sold and non canceled market items by category
     * @param categoryUid
     */
    @Throws(FetchAvailableMarketItemsByCategoryDataException::class)
    suspend fun fetchAvailableMarketItemsByCategory(categoryUid: String): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are being listed by the current authenticated user
     * @param limit
     */
    @Throws(FetchSellingMarketItemsDataException::class)
    suspend fun fetchSellingMarketItems(limit: Int? = null): Iterable<ArtCollectibleForSale>

    /**
     * Fetch market items that are owned by the current authenticated user
     * @param limit
     */
    @Throws(FetchOwnedMarketItemsDataException::class)
    suspend fun fetchOwnedMarketItems(limit: Int? = null): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch market history
     * @param limit
     */
    @Throws(FetchMarketHistoryDataException::class)
    suspend fun fetchMarketHistory(limit: Int? = null): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch token market history
     * @param tokenId
     * @param limit
     */
    @Throws(FetchMarketHistoryDataException::class)
    suspend fun fetchTokenMarketHistory(tokenId: BigInteger, limit: Int? = null): Iterable<ArtCollectibleForSale>

    /**
     * Allow us to fetch token current princes
     * @param tokenId
     */
    @Throws(FetchTokenCurrentPriceDataException::class)
    suspend fun fetchTokenCurrentPrice(tokenId: BigInteger): ArtCollectiblePrices

    /**
     * Put item for Sale
     * @param tokenId
     * @param priceInEth
     */
    @Throws(PutItemForSaleDataException::class)
    suspend fun putItemForSale(tokenId: BigInteger, priceInEth: Float)

    /**
     * Fetch item for Sale
     * @param tokenId
     */
    @Throws(FetchItemForSaleDataException::class)
    suspend fun fetchItemForSale(tokenId: BigInteger): ArtCollectibleForSale

    /**
     * Fetch market history item
     * @param marketItemId
     */
    @Throws(FetchMarketHistoryItemDataException::class)
    suspend fun fetchMarketHistoryItem(marketItemId: BigInteger): ArtCollectibleForSale

    /**
     * Withdraw from sale
     * @param tokenId
     */
    @Throws(WithdrawFromSaleDataException::class)
    suspend fun withdrawFromSale(tokenId: BigInteger)

    /**
     * Is Token Added for sale
     * @param tokenId
     */
    @Throws(CheckTokenAddedForSaleDataException::class)
    suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean

    /**
     * Fetch Marketplace statistics
     */
    @Throws(FetchMarketplaceStatisticsDataException::class)
    suspend fun fetchMarketplaceStatistics(): MarketplaceStatistics

    /**
     * Fetch Token market history prices
     * @param tokenId
     */
    @Throws(FetchTokenMarketHistoryPricesDataException::class)
    suspend fun fetchTokenMarketHistoryPrices(tokenId: BigInteger): Iterable<ArtCollectibleMarketHistoryPrice>

    /**
     * Buy item
     * @param tokenId
     */
    @Throws(BuyItemDataException::class)
    suspend fun buyItem(tokenId: BigInteger)

    /**
     * Get similar market items
     * @param tokenId
     * @param count
     */
    @Throws(GetSimilarMarketItemsDataException::class)
    suspend fun getSimilarMarketItems(tokenId: BigInteger, count: Int): Iterable<ArtCollectibleForSale>

    /**
     * Get similar author market items
     * @param tokenId
     * @param count
     */
    @Throws(GetSimilarAuthorMarketItemsDataException::class)
    suspend fun getSimilarAuthorMarketItems(tokenId: BigInteger, count: Int): Iterable<ArtCollectibleForSale>
}