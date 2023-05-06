package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.core.SupportBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.*
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.*
import com.dreamsoftware.artcollectibles.data.blockchain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

internal class ArtMarketplaceBlockchainDataSourceImpl(
    private val artMarketplaceMapper: ArtMarketplaceMapper,
    private val marketStatisticsMapper: MarketStatisticsMapper,
    private val walletStatisticsMapper: WalletStatisticsMapper,
    private val artCollectibleMarketPriceMapper: ArtCollectibleMarketPriceMapper,
    private val artCollectibleForSalePricesMapper: ArtCollectibleForSalePricesMapper,
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j,
) : SupportBlockchainDataSource(blockchainConfig, web3j), IArtMarketplaceBlockchainDataSource {

    private companion object {
        private val DEFAULT_COST_OF_PUTTING_FOR_SALE_IN_ETH = BigDecimal.valueOf(0.010)
    }

    private enum class MarketItemType { AVAILABLE, SELLING, OWNED, HISTORY }

    @Throws(FetchAvailableMarketItemsException::class)
    override suspend fun fetchAvailableMarketItems(
        credentials: Credentials,
        limit: Int?
    ): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            try {
                fetchMarketItemsBy(
                    type = MarketItemType.AVAILABLE,
                    credentials = credentials,
                    limit = limit
                )
            } catch (ex: Exception) {
                throw FetchAvailableMarketItemsException(
                    "An error occurred when trying to fetch available market items",
                    ex
                )
            }
        }

    @Throws(FetchSellingMarketItemsException::class)
    override suspend fun fetchSellingMarketItems(
        credentials: Credentials,
        limit: Int?
    ): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            try {
                fetchMarketItemsBy(
                    type = MarketItemType.SELLING,
                    credentials = credentials,
                    limit = limit
                )
            } catch (ex: Exception) {
                throw FetchSellingMarketItemsException(
                    "An error occurred when trying to fetch selling market items",
                    ex
                )
            }
        }

    @Throws(FetchOwnedMarketItemsException::class)
    override suspend fun fetchOwnedMarketItems(
        credentials: Credentials,
        limit: Int?
    ): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            try {
                fetchMarketItemsBy(
                    type = MarketItemType.OWNED,
                    credentials = credentials,
                    limit = limit
                )
            } catch (ex: Exception) {
                throw FetchOwnedMarketItemsException(
                    "An error occurred when trying to fetch owned market items",
                    ex
                )
            }
        }

    @Throws(FetchMarketHistoryException::class)
    override suspend fun fetchMarketHistory(
        credentials: Credentials,
        limit: Int?
    ): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            try {
                fetchMarketItemsBy(
                    type = MarketItemType.HISTORY,
                    credentials = credentials,
                    limit = limit
                )
            } catch (ex: Exception) {
                throw FetchMarketHistoryException(
                    "An error occurred when trying to fetch market history",
                    ex
                )
            }
        }

    @Throws(PutItemForSaleException::class)
    override suspend fun putItemForSale(
        tokenId: BigInteger,
        priceInEth: Float,
        credentials: Credentials
    ): Unit =
        withContext(Dispatchers.IO) {
            try {
                with(loadContract(credentials)) {
                    val defaultCostOfPuttingForSale =
                        Convert.toWei(DEFAULT_COST_OF_PUTTING_FOR_SALE_IN_ETH, Convert.Unit.ETHER)
                            .toBigInteger()
                    val putItemForSalePriceInWei =
                        Convert.toWei(priceInEth.toString(), Convert.Unit.ETHER).toBigInteger()
                    putItemForSale(
                        tokenId,
                        putItemForSalePriceInWei,
                        defaultCostOfPuttingForSale
                    ).send()
                }
            } catch (ex: Exception) {
                throw PutItemForSaleException("An error occurred when trying to put an item for sale", ex)
            }
        }

    @Throws(WithdrawFromSaleException::class)
    override suspend fun withdrawFromSale(tokenId: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            try {
                loadContract(credentials).withdrawFromSale(tokenId).send()
            } catch (ex: Exception) {
                throw WithdrawFromSaleException("An error occurred when trying to withdraw from sale", ex)
            }
        }
    }

    @Throws(BuyItemException::class)
    override suspend fun buyItem(tokenId: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            try {
                with(loadContract(credentials)) {
                    if (isTokenAddedForSale(tokenId).send()) {
                        throw ItemNotAvailableForSale("Token id $tokenId is not available for sale")
                    }
                    val itemForSale = fetchItemForSale(tokenId).send()
                    buyItem(tokenId, itemForSale.price).send()
                }
            } catch (ex: Exception) {
                throw BuyItemException("An error occurred when trying to buy an item", ex)
            }
        }
    }

    @Throws(FetchItemForSaleException::class)
    override suspend fun fetchItemForSale(
        tokenId: BigInteger,
        credentials: Credentials
    ): ArtCollectibleForSaleDTO = withContext(Dispatchers.IO) {
        try {
            val itemForSale = loadContract(credentials).fetchItemForSale(tokenId).send()
            artMarketplaceMapper.mapInToOut(itemForSale)
        } catch (ex: Exception) {
            throw FetchItemForSaleException("An error occurred when trying to fetch an item for sale", ex)
        }
    }

    @Throws(FetchMarketHistoryItemException::class)
    override suspend fun fetchMarketHistoryItem(
        marketItemId: BigInteger,
        credentials: Credentials
    ): ArtCollectibleForSaleDTO = withContext(Dispatchers.IO) {
        try {
            val itemForSale = loadContract(credentials).fetchMarketHistoryItem(marketItemId).send()
            artMarketplaceMapper.mapInToOut(itemForSale)
        } catch (ex: Exception) {
            throw FetchMarketHistoryItemException("An error occurred when trying to fetch a token market history", ex)
        }
    }

    @Throws(FetchItemForSaleByMetadataCIDException::class)
    override suspend fun fetchItemForSaleByMetadataCID(
        cid: String,
        credentials: Credentials
    ): ArtCollectibleForSaleDTO = withContext(Dispatchers.IO) {
        try {
            val itemForSale = loadContract(credentials).fetchItemForSaleByMetadataCID(cid).send()
            artMarketplaceMapper.mapInToOut(itemForSale)
        } catch (ex: Exception) {
            throw FetchItemForSaleByMetadataCIDException("An error occurred when trying to fetch item for sale by CID", ex)
        }
    }

    @Throws(CheckTokenAddedForSaleException::class)
    override suspend fun isTokenAddedForSale(
        tokenId: BigInteger,
        credentials: Credentials
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                loadContract(credentials).isTokenAddedForSale(tokenId).send()
            } catch (ex: Exception) {
                throw CheckTokenAddedForSaleException("An error occurred when trying to check if token was added for sale", ex)
            }
        }

    @Throws(FetchMarketplaceStatisticsException::class)
    override suspend fun fetchMarketplaceStatistics(credentials: Credentials): MarketplaceStatisticsDTO =
        withContext(Dispatchers.IO) {
            try {
                marketStatisticsMapper.mapInToOut(
                    loadContract(credentials).fetchMarketStatistics().send()
                )
            } catch (ex: Exception) {
               throw FetchMarketplaceStatisticsException("An error occurred when trying to fetch marketplace statistics", ex)
            }
        }

    @Throws(FetchWalletStatisticsException::class)
    override suspend fun fetchWalletStatistics(
        credentials: Credentials,
        ownerAddress: String
    ): WalletStatisticsDTO =
        withContext(Dispatchers.IO) {
            try {
                walletStatisticsMapper.mapInToOut(
                    loadContract(credentials).fetchWalletStatistics(
                        ownerAddress
                    ).send()
                )
            } catch (ex: Exception) {
                throw FetchWalletStatisticsException("An error occurred when trying to fetch wallet statistics", ex)
            }
        }

    @Throws(CheckTokenAddedForSaleException::class)
    override suspend fun isTokenCIDAddedForSale(cid: String, credentials: Credentials): Boolean =
        withContext(Dispatchers.IO) {
            try {
                loadContract(credentials).isTokenMetadataCIDAddedForSale(cid).send()
            } catch (ex: Exception) {
                throw CheckTokenAddedForSaleException("An error occurred when trying to check if a token was added for sale", ex)
            }
        }

    @Throws(FetchTokenMarketHistoryException::class)
    override suspend fun fetchTokenMarketHistory(
        tokenId: BigInteger,
        credentials: Credentials,
        limit: Int?
    ): Iterable<ArtCollectibleForSaleDTO> = withContext(Dispatchers.IO) {
        try {
            with(loadContract(credentials)) {
                val contractCall = limit?.toBigInteger()?.let {
                    fetchPaginatedTokenMarketHistory(tokenId, it)
                } ?: fetchTokenMarketHistory(tokenId)
                val marketItems = contractCall
                    .send()
                    .filterIsInstance<ArtCollectibleForSale>()
                artMarketplaceMapper.mapInListToOutList(marketItems)
            }
        } catch (ex: Exception) {
            throw FetchTokenMarketHistoryException("An error occurred when trying to fetch ", ex)
        }
    }

    @Throws(FetchCurrentItemPriceException::class)
    override suspend fun fetchCurrentItemPrice(
        tokenId: BigInteger,
        credentials: Credentials
    ): ArtCollectibleForSalePricesDTO = withContext(Dispatchers.IO) {
        try {
            val price = loadContract(credentials).fetchCurrentItemPrice(tokenId).send()
            artCollectibleForSalePricesMapper.mapInToOut(price)
        } catch (ex: Exception) {
            throw FetchCurrentItemPriceException("An error occurred when trying to fetch current item price", ex)
        }
    }

    @Throws(FetchTokenMarketHistoryPricesException::class)
    override suspend fun fetchTokenMarketHistoryPrices(
        tokenId: BigInteger,
        credentials: Credentials
    ): Iterable<ArtCollectibleMarketHistoryPriceDTO> = withContext(Dispatchers.IO) {
        try {
            val marketPrices = loadContract(credentials)
                .fetchTokenMarketHistoryPrices(tokenId)
                .send() as List<ArtMarketplaceContract.ArtCollectibleMarketPrice>
            artCollectibleMarketPriceMapper.mapInListToOutList(marketPrices)
        } catch (ex: Exception) {
            throw FetchTokenMarketHistoryPricesException("An error occurred when trying to fetch token market history prices", ex)
        }
    }

    private fun fetchMarketItemsBy(
        type: MarketItemType,
        credentials: Credentials,
        limit: Int? = null
    ) =
        with(loadContract(credentials)) {
            val marketItems = when (type) {
                MarketItemType.AVAILABLE -> limit?.toBigInteger()?.let {
                    fetchPaginatedAvailableMarketItems(it)
                } ?: fetchAvailableMarketItems()
                MarketItemType.SELLING -> limit?.toBigInteger()?.let {
                    fetchPaginatedSellingMarketItems(it)
                } ?: fetchSellingMarketItems()
                MarketItemType.OWNED -> limit?.toBigInteger()?.let {
                    fetchPaginatedOwnedMarketItems(it)
                } ?: fetchOwnedMarketItems()
                MarketItemType.HISTORY -> limit?.toBigInteger()?.let {
                    fetchLastMarketHistoryItems(it)
                } ?: fetchMarketHistory()
            }.send().filterIsInstance<ArtCollectibleForSale>()
            artMarketplaceMapper.mapInListToOutList(marketItems)
        }

    private fun loadContract(credentials: Credentials): ArtMarketplaceContract =
        with(blockchainConfig) {
            val txManager = FastRawTransactionManager(web3j, credentials, chainId)
            ArtMarketplaceContract.load(
                artMarketplaceContractAddress,
                web3j,
                txManager,
                gasProvider
            )
        }
}