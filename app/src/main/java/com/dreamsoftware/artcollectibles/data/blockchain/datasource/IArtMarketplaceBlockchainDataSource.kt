package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import java.math.BigInteger

interface IArtMarketplaceBlockchainDataSource {

    /**
     * Fetch non sold and non canceled market items
     */
    suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSaleEntity>

    /**
     * Fetch market items that are being listed by the current authenticated user
     */
    suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSaleEntity>

    /**
     * Fetch market items that are owned by the current authenticated user
     */
    suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSaleEntity>

    /**
     * Allow us to fetch market history
     */
    suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSaleEntity>

    /**
     * list an item with a `tokenId` for a `price`
     * @param tokenId
     * @param price
     */
    suspend fun putItemForSale(tokenId: BigInteger, price: BigInteger): BigInteger

    /**
     * Cancel a listing of an item with a `tokenId`
     * @param tokenId
     */
    suspend fun withdrawFromSale(tokenId: BigInteger)

    /**
     * Buy an item with a `tokenId` and pay the owner and the creator
     * @param tokenId
     * @param price
     */
    suspend fun buyItem(tokenId: BigInteger, price: BigInteger)

}