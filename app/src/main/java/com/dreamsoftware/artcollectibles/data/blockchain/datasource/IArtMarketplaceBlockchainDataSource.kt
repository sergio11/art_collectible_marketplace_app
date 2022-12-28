package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import org.web3j.crypto.Credentials
import java.math.BigInteger

interface IArtMarketplaceBlockchainDataSource {

    /**
     * Fetch non sold and non canceled market items
     * @param credentials
     */
    suspend fun fetchAvailableMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleEntity>

    /**
     * Fetch market items that are being listed by the current authenticated user
     * @param credentials
     */
    suspend fun fetchSellingMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleEntity>

    /**
     * Fetch market items that are owned by the current authenticated user
     * @param credentials
     */
    suspend fun fetchOwnedMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleEntity>

    /**
     * Allow us to fetch market history
     * @param credentials
     */
    suspend fun fetchMarketHistory(credentials: Credentials): Iterable<ArtCollectibleForSaleEntity>

    /**
     * list an item with a `tokenId` for a `price`
     * @param tokenId
     * @param price
     * @param credentials
     */
    suspend fun putItemForSale(tokenId: BigInteger, price: BigInteger, credentials: Credentials): BigInteger

    /**
     * Cancel a listing of an item with a `tokenId`
     * @param tokenId
     * @param credentials
     */
    suspend fun withdrawFromSale(tokenId: BigInteger, credentials: Credentials)

    /**
     * Buy an item with a `tokenId` and pay the owner and the creator
     * @param tokenId
     * @param price
     * @param credentials
     */
    suspend fun buyItem(tokenId: BigInteger, price: BigInteger, credentials: Credentials)

}