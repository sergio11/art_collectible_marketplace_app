package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketplaceStatisticsDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.WalletStatisticsDTO
import org.web3j.crypto.Credentials
import java.math.BigInteger

interface IArtMarketplaceBlockchainDataSource {

    /**
     * Fetch non sold and non canceled market items
     * @param credentials
     */
    suspend fun fetchAvailableMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Fetch market items that are being listed by the current authenticated user
     * @param credentials
     */
    suspend fun fetchSellingMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Fetch market items that are owned by the current authenticated user
     * @param credentials
     */
    suspend fun fetchOwnedMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Allow us to fetch market history
     * @param credentials
     */
    suspend fun fetchMarketHistory(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO>

    /**
     * list an item with a `tokenId` for a `priceInEth`
     * @param tokenId
     * @param priceInEth
     * @param credentials
     */
    suspend fun putItemForSale(tokenId: BigInteger, priceInEth: Float, credentials: Credentials)

    /**
     * Cancel a listing of an item with a `tokenId`
     * @param tokenId
     * @param credentials
     */
    suspend fun withdrawFromSale(tokenId: BigInteger, credentials: Credentials)

    /**
     * Buy an item with a `tokenId` and pay the owner and the creator
     * @param tokenId
     * @param credentials
     */
    suspend fun buyItem(tokenId: BigInteger, credentials: Credentials)

    /**
     * Fetch item for sale
     * @param tokenId
     * @param credentials
     */
    suspend fun fetchItemForSale(tokenId: BigInteger, credentials: Credentials): ArtCollectibleForSaleDTO

    /**
     * Fetch item for sale by metadata CID
     * @param cid
     * @param credentials
     */
    suspend fun fetchItemForSaleByMetadataCID(cid: String, credentials: Credentials): ArtCollectibleForSaleDTO

    /**
     * Is token added for sale
     * @param tokenId
     * @param credentials
     */
    suspend fun isTokenAddedForSale(tokenId: BigInteger, credentials: Credentials): Boolean

    /**
     * Fetch Marketplace statistics
     * @param credentials
     */
    suspend fun fetchMarketplaceStatistics(credentials: Credentials): MarketplaceStatisticsDTO

    /**
     * Fetch Wallet Statistics
     * @param credentials
     * @param ownerAddress
     */
    suspend fun fetchWalletStatistics(credentials: Credentials, ownerAddress: String): WalletStatisticsDTO

    /**
     * Check if token CID added for sale
     * @param cid
     * @param credentials
     */
    suspend fun isTokenCIDAddedForSale(cid: String, credentials: Credentials): Boolean

    /**
     * Fetch token market history
     * @param tokenId
     * @param credentials
     */
    suspend fun fetchTokenMarketHistory(tokenId: BigInteger, credentials: Credentials): Iterable<ArtCollectibleForSaleDTO>
}