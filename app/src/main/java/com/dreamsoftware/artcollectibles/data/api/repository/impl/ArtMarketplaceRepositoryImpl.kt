package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMarketHistoryPriceMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.MarketplaceStatisticsMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IMarketPricesBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSalePricesDTO
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtMarketItemMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketHistoryPrice
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectiblePrices
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import com.google.common.collect.Iterables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.math.BigInteger

/**
 * Art Marketplace Repository Impl
 * @param artMarketplaceBlockchainDataSource
 * @param userRepository
 * @param artCollectibleRepository
 * @param walletRepository
 * @param userCredentialsMapper
 * @param marketplaceStatisticsMapper
 * @param artCollectibleMarketHistoryPriceMapper
 * @param categoriesDataSource
 * @param marketPricesBlockchainDataSource
 * @param artMarketItemMemoryCacheDataSource
 */
internal class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val marketplaceStatisticsMapper: MarketplaceStatisticsMapper,
    private val artCollectibleMarketHistoryPriceMapper: ArtCollectibleMarketHistoryPriceMapper,
    private val categoriesDataSource: ICategoriesDataSource,
    private val marketPricesBlockchainDataSource: IMarketPricesBlockchainDataSource,
    private val artMarketItemMemoryCacheDataSource: IArtMarketItemMemoryCacheDataSource
) : IArtMarketplaceRepository {

    @Throws(FetchAvailableMarketItemsDataException::class)
    override suspend fun fetchAvailableMarketItems(limit: Int?): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                mapToArtCollectibleForSaleList(artMarketplaceBlockchainDataSource.fetchAvailableMarketItems(
                    credentials = userCredentialsMapper.mapOutToIn(credentials),
                    limit = limit
                ))
            } catch (ex: Exception) {
                throw FetchAvailableMarketItemsDataException(
                    "An error occurred when fetching available market items",
                    ex
                )
            }
        }

    @Throws(FetchAvailableMarketItemsByCategoryDataException::class)
    override suspend fun fetchAvailableMarketItemsByCategory(categoryUid: String): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                with(artMarketplaceBlockchainDataSource) {
                    categoriesDataSource.getTokensByUid(categoryUid)
                        .toMutableList()
                        .asFlow()
                        .filter { cid ->
                            isTokenCIDAddedForSale(
                                cid,
                                userCredentialsMapper.mapOutToIn(credentials)
                            )
                        }
                        .map { cid ->
                            fetchItemForSaleByMetadataCID(
                                cid,
                                userCredentialsMapper.mapOutToIn(credentials)
                            )
                        }
                        .toList()
                        .let {
                            mapToArtCollectibleForSaleList(it)
                        }
                }
            } catch (ex: Exception) {
                throw FetchAvailableMarketItemsByCategoryDataException("An error occurred when fetching available market items", ex)
            }
        }

    @Throws(FetchSellingMarketItemsDataException::class)
    override suspend fun fetchSellingMarketItems(limit: Int?): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val sellingMarketItems = artMarketplaceBlockchainDataSource.fetchSellingMarketItems(
                    credentials = userCredentialsMapper.mapOutToIn(credentials),
                    limit = limit
                )
                mapToArtCollectibleForSaleList(sellingMarketItems)
            } catch (ex: Exception) {
                throw FetchSellingMarketItemsDataException(
                    "An error occurred when fetching selling market items",
                    ex
                )
            }
        }

    @Throws(FetchOwnedMarketItemsDataException::class)
    override suspend fun fetchOwnedMarketItems(limit: Int?): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                mapToArtCollectibleForSaleList(artMarketplaceBlockchainDataSource.fetchOwnedMarketItems(
                    credentials = userCredentialsMapper.mapOutToIn(credentials),
                    limit = limit
                ))
            } catch (ex: Exception) {
                throw FetchOwnedMarketItemsDataException(
                    "An error occurred when fetching owned market items",
                    ex
                )
            }
        }

    @Throws(FetchMarketHistoryDataException::class)
    override suspend fun fetchMarketHistory(limit: Int?): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                mapToArtCollectibleForSaleList(artMarketplaceBlockchainDataSource.fetchMarketHistory(
                    credentials = userCredentialsMapper.mapOutToIn(credentials),
                    limit = limit
                ))
            } catch (ex: Exception) {
                throw FetchMarketHistoryDataException(
                    "An error occurred when fetching market history",
                    ex
                )
            }
        }

    @Throws(FetchMarketHistoryDataException::class)
    override suspend fun fetchTokenMarketHistory(tokenId: BigInteger, limit: Int?): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                mapToArtCollectibleForSaleList(artMarketplaceBlockchainDataSource.fetchTokenMarketHistory(
                    tokenId = tokenId,
                    credentials = userCredentialsMapper.mapOutToIn(credentials),
                    limit = limit
                ))
            } catch (ex: Exception) {
                throw FetchMarketHistoryDataException(
                    "An error occurred when fetching the token market history",
                    ex
                )
            }
        }

    @Throws(FetchTokenCurrentPriceDataException::class)
    override suspend fun fetchTokenCurrentPrice(tokenId: BigInteger): ArtCollectiblePrices = withContext(Dispatchers.IO) {
        try {
            val credentials = walletRepository.loadCredentials()
            getArtCollectiblePrices(artMarketplaceBlockchainDataSource.fetchCurrentItemPrice(
                tokenId = tokenId,
                credentials = userCredentialsMapper.mapOutToIn(credentials)))
        } catch (ex: Exception) {
            throw FetchMarketHistoryDataException(
                "An error occurred when fetching the token current price",
                ex
            )
        }
    }

    @Throws(PutItemForSaleDataException::class)
    override suspend fun putItemForSale(tokenId: BigInteger, priceInEth: Float) =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.putItemForSale(
                    tokenId = tokenId,
                    priceInEth = priceInEth,
                    credentials = userCredentialsMapper.mapOutToIn(credentials)
                )
            } catch (ex: Exception) {
                throw PutItemForSaleDataException(
                    "An error occurred when trying to put item for sale",
                    ex
                )
            }
        }

    @Throws(FetchItemForSaleDataException::class)
    override suspend fun fetchItemForSale(tokenId: BigInteger): ArtCollectibleForSale =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                with(artMarketItemMemoryCacheDataSource) {
                    try {
                        findByKey("token_id_$tokenId")
                    } catch (ex: CacheException) {
                        val artCollectible = artMarketplaceBlockchainDataSource.fetchItemForSale(
                            tokenId = tokenId,
                            credentials = userCredentialsMapper.mapOutToIn(credentials)
                        )
                        mapToArtCollectibleForSale(artCollectible, true).also {
                            save("token_id_${it.token.id}", it)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw FetchItemForSaleDataException(
                    "An error occurred when trying to fetch item for sale",
                    ex
                )
            }
        }

    @Throws(FetchMarketHistoryItemDataException::class)
    override suspend fun fetchMarketHistoryItem(marketItemId: BigInteger): ArtCollectibleForSale = withContext(Dispatchers.IO) {
        try {
            val credentials = walletRepository.loadCredentials()
            with(artMarketItemMemoryCacheDataSource) {
                try {
                    findByKey(marketItemId)
                } catch (ex: CacheException) {
                    val artCollectible = artMarketplaceBlockchainDataSource.fetchMarketHistoryItem(
                        marketItemId = marketItemId,
                        credentials = userCredentialsMapper.mapOutToIn(credentials)
                    )
                    mapToArtCollectibleForSale(artCollectible, true).also {
                        save(marketItemId, it)
                    }
                }
            }
        } catch (ex: Exception) {
            throw FetchMarketHistoryItemDataException(
                "An error occurred when trying to fetch market history item",
                ex
            )
        }
    }

    @Throws(WithdrawFromSaleDataException::class)
    override suspend fun withdrawFromSale(tokenId: BigInteger) = withContext(Dispatchers.IO) {
        try {
            val credentials = walletRepository.loadCredentials()
            artMarketplaceBlockchainDataSource.withdrawFromSale(
                tokenId = tokenId,
                credentials = userCredentialsMapper.mapOutToIn(credentials)
            ).also {
                artMarketItemMemoryCacheDataSource.delete("token_id_$tokenId")
            }
        } catch (ex: Exception) {
            throw WithdrawFromSaleDataException(
                "An error occurred when trying to withdraw item from sale",
                ex
            )
        }
    }

    @Throws(CheckTokenAddedForSaleDataException::class)
    override suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.isTokenAddedForSale(
                    tokenId = tokenId,
                    credentials = userCredentialsMapper.mapOutToIn(credentials)
                )
            } catch (ex: Exception) {
                throw CheckTokenAddedForSaleDataException(
                    "An error occurred when checking if token has been added for sale",
                    ex
                )
            }
        }

    @Throws(FetchMarketplaceStatisticsDataException::class)
    override suspend fun fetchMarketplaceStatistics(): MarketplaceStatistics =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val marketplaceStatisticsDTO =
                    artMarketplaceBlockchainDataSource.fetchMarketplaceStatistics(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                marketplaceStatisticsMapper.mapInToOut(marketplaceStatisticsDTO)
            } catch (ex: Exception) {
                throw FetchMarketplaceStatisticsDataException(
                    "An error occurred when fetching marketplace statistics",
                    ex
                )
            }
        }

    @Throws(FetchTokenMarketHistoryPricesDataException::class)
    override suspend fun fetchTokenMarketHistoryPrices(tokenId: BigInteger): Iterable<ArtCollectibleMarketHistoryPrice> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectible = artCollectibleRepository.getTokenById(tokenId)
                artMarketplaceBlockchainDataSource.fetchTokenMarketHistoryPrices(
                    tokenId = tokenId,
                    credentials = userCredentialsMapper.mapOutToIn(credentials)
                ).map {
                    artCollectibleMarketHistoryPriceMapper.mapInToOut(ArtCollectibleMarketHistoryPriceMapper.InputData(
                        artCollectible = artCollectible,
                        artCollectibleMarketHistoryPriceDTO = it
                    ))
                }
            } catch (ex: Exception) {
                throw FetchTokenMarketHistoryPricesDataException(
                    "An error occurred when fetching market history prices",
                    ex
                )
            }
        }

    @Throws(BuyItemDataException::class)
    override suspend fun buyItem(tokenId: BigInteger) {
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.buyItem(
                    tokenId = tokenId,
                    credentials = userCredentialsMapper.mapOutToIn(credentials)
                ).also {
                    artMarketItemMemoryCacheDataSource.delete("token_id_$tokenId")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw BuyItemDataException("An error occurred when trying to buy an item", ex)
            }
        }
    }

    @Throws(GetSimilarMarketItemsDataException::class)
    override suspend fun getSimilarMarketItems(
        tokenId: BigInteger,
        count: Int
    ): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        try {
            val artCollectible = artCollectibleRepository.getTokenById(tokenId)
            val credentials = walletRepository.loadCredentials()
            with(artMarketplaceBlockchainDataSource) {
                categoriesDataSource.getTokensByUid(artCollectible.metadata.category.uid)
                    .filter { it != artCollectible.metadata.cid }
                    .toMutableList()
                    .apply {
                        shuffle()
                    }
                    .asFlow()
                    .filter { cid ->
                        isTokenCIDAddedForSale(
                            cid,
                            userCredentialsMapper.mapOutToIn(credentials)
                        )
                    }
                    .take(count)
                    .map { cid ->
                        fetchItemForSaleByMetadataCID(
                            cid,
                            userCredentialsMapper.mapOutToIn(credentials)
                        )
                    }
                    .toList()
                    .let {
                        mapToArtCollectibleForSaleList(it)
                    }
            }
        } catch (ex: Exception) {
            throw GetMarketItemsByCategoryDataException(
                "An error occurred when trying to get similar market items",
                ex
            )
        }
    }

    @Throws(GetSimilarAuthorMarketItemsDataException::class)
    override suspend fun getSimilarAuthorMarketItems(
        tokenId: BigInteger,
        count: Int
    ): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        try {
            val artCollectible = artCollectibleRepository.getTokenById(tokenId)
            val credentials = walletRepository.loadCredentials()
            artMarketplaceBlockchainDataSource.fetchAvailableMarketItems(userCredentialsMapper.mapOutToIn(credentials))
                .filter { it.creator == artCollectible.author.walletAddress && it.tokenId != artCollectible.id }
                .take(count)
                .let { mapToArtCollectibleForSaleList(it) }
        } catch (ex: Exception) {
            throw GetMarketItemsByCategoryDataException(
                "An error occurred when trying to get similar author market items",
                ex
            )
        }
    }

    private suspend fun mapToArtCollectibleForSale(item: ArtCollectibleForSaleDTO, requireUserInfoFullDetail: Boolean): ArtCollectibleForSale =
        withContext(Dispatchers.Default) {
            with(item) {
                val tokenDeferred = async {
                    artCollectibleRepository.getTokenById(tokenId)
                }
                val ownerDeferred = async {
                    runCatching {
                        userRepository.getByAddress(owner, requireUserInfoFullDetail)
                    }.getOrNull()
                }
                val sellerDeferred = async { userRepository.getByAddress(seller, requireUserInfoFullDetail) }
                ArtCollectibleForSale(
                    marketItemId = marketItemId,
                    token = tokenDeferred.await(),
                    seller = sellerDeferred.await(),
                    owner = ownerDeferred.await(),
                    price = getArtCollectiblePrices(prices),
                    sold = sold,
                    canceled = canceled,
                    putForSaleAt = putForSaleAt,
                    soldAt = soldAt,
                    canceledAt = canceledAt
                )
            }
        }


    private suspend fun mapToArtCollectibleForSaleList(items: Iterable<ArtCollectibleForSaleDTO>): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.Default) {
            Log.d("ART_COLL", "mapToArtCollectibleForSaleList -> items: ${Iterables.size(items)} CALLED!")
            items.forEach {
                Log.d("ART_COLL", "it.tokenId -> ${it.tokenId}, t.metadataCID -> ${it.metadataCID}")
            }
            with(artMarketItemMemoryCacheDataSource) {
                val marketItemsCached = runCatching {
                    findByKeys(
                        keys = items.map { it.marketItemId },
                        strictMode = false
                    )
                }.getOrDefault(emptyList())
                if(Iterables.size(marketItemsCached) != Iterables.size(items)) {
                    val marketItemIdsCached = marketItemsCached.map { it.marketItemId }
                    val itemsNotCached = items.filterNot { marketItemIdsCached.contains(it.marketItemId) }
                    val tokensData = artCollectibleRepository.getTokens(itemsNotCached.map(ArtCollectibleForSaleDTO::tokenId))
                    itemsNotCached.asSequence().map { itemForSale ->
                        async {
                            with(itemForSale) {
                                tokensData.find { it.id == tokenId }?.let { token ->
                                    val owner = runCatching {
                                        userRepository.getByAddress(owner, false)
                                    }.getOrNull()
                                    val seller = userRepository.getByAddress(seller, false)
                                    ArtCollectibleForSale(
                                        marketItemId = marketItemId,
                                        token = token,
                                        seller = seller,
                                        owner = owner,
                                        price = getArtCollectiblePrices(prices),
                                        sold = sold,
                                        canceled = canceled,
                                        putForSaleAt = putForSaleAt,
                                        soldAt = soldAt,
                                        canceledAt = canceledAt
                                    )
                                }
                            }
                        }
                    }.toList().awaitAll().filterNotNull().onEach {
                        save(it.marketItemId, it)
                    }.let {
                        marketItemsCached + it
                    }
                } else {
                    marketItemsCached
                }
            }
        }


    private suspend fun getArtCollectiblePrices(artCollectiblePrices: ArtCollectibleForSalePricesDTO) =  with(artCollectiblePrices) {
        runCatching {
            marketPricesBlockchainDataSource.getPricesOf(priceInEth).let {
                ArtCollectiblePrices(
                    priceInEUR = it.priceInEUR,
                    priceInUSD = it.priceInUSD,
                    priceInWei = priceInWei,
                    priceInEth = priceInEth
                )
            }
        }.getOrDefault(
            ArtCollectiblePrices(
                priceInWei = priceInWei,
                priceInEth = priceInEth
            )
        )
    }
}