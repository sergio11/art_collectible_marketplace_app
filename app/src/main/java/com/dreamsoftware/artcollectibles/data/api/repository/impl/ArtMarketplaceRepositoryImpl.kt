package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.MarketplaceStatisticsMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IMarketPricesBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICategoriesDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectiblePrices
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Art Marketplace Repository Impl
 * @param artMarketplaceBlockchainDataSource
 * @param userRepository
 * @param artCollectibleRepository
 * @param walletRepository
 * @param userCredentialsMapper
 * @param marketplaceStatisticsMapper
 * @param categoriesDataSource
 * @param marketPricesBlockchainDataSource
 */
internal class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val userRepository: IUserRepository,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val marketplaceStatisticsMapper: MarketplaceStatisticsMapper,
    private val categoriesDataSource: ICategoriesDataSource,
    private val marketPricesBlockchainDataSource: IMarketPricesBlockchainDataSource
) : IArtMarketplaceRepository {

    @Throws(FetchAvailableMarketItemsException::class)
    override suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectibleForSaleList =
                    artMarketplaceBlockchainDataSource.fetchAvailableMarketItems(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                mapToArtCollectibleForSaleList(artCollectibleForSaleList)
            } catch (ex: Exception) {
                throw FetchAvailableMarketItemsException(
                    "An error occurred when fetching available market items",
                    ex
                )
            }
        }

    @Throws(FetchSellingMarketItemsException::class)
    override suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectibleForSaleList =
                    artMarketplaceBlockchainDataSource.fetchSellingMarketItems(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                mapToArtCollectibleForSaleList(artCollectibleForSaleList)
            } catch (ex: Exception) {
                throw FetchSellingMarketItemsException(
                    "An error occurred when fetching selling market items",
                    ex
                )
            }
        }

    @Throws(FetchOwnedMarketItemsException::class)
    override suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectibleForSaleList =
                    artMarketplaceBlockchainDataSource.fetchOwnedMarketItems(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                mapToArtCollectibleForSaleList(artCollectibleForSaleList)
            } catch (ex: Exception) {
                throw FetchOwnedMarketItemsException(
                    "An error occurred when fetching owned market items",
                    ex
                )
            }
        }

    @Throws(FetchMarketHistoryException::class)
    override suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectibleForSaleList =
                    artMarketplaceBlockchainDataSource.fetchMarketHistory(
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                mapToArtCollectibleForSaleList(artCollectibleForSaleList)
            } catch (ex: Exception) {
                throw FetchMarketHistoryException(
                    "An error occurred when fetching market history",
                    ex
                )
            }
        }

    @Throws(FetchMarketHistoryException::class)
    override suspend fun fetchTokenMarketHistory(tokenId: BigInteger): Iterable<ArtCollectibleForSale> =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectibleForSaleList =
                    artMarketplaceBlockchainDataSource.fetchTokenMarketHistory(
                        tokenId,
                        userCredentialsMapper.mapOutToIn(credentials)
                    )
                mapToArtCollectibleForSaleList(artCollectibleForSaleList)
            } catch (ex: Exception) {
                throw FetchMarketHistoryException(
                    "An error occurred when fetching the token market history",
                    ex
                )
            }
        }

    @Throws(PutItemForSaleException::class)
    override suspend fun putItemForSale(tokenId: BigInteger, priceInEth: Float) =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.putItemForSale(
                    tokenId,
                    priceInEth,
                    userCredentialsMapper.mapOutToIn(credentials)
                )
            } catch (ex: Exception) {
                throw PutItemForSaleException(
                    "An error occurred when trying to put item for sale",
                    ex
                )
            }
        }

    @Throws(FetchItemForSaleException::class)
    override suspend fun fetchItemForSale(tokenId: BigInteger): ArtCollectibleForSale =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                val artCollectible = artMarketplaceBlockchainDataSource.fetchItemForSale(
                    tokenId,
                    userCredentialsMapper.mapOutToIn(credentials)
                )
                mapToArtCollectibleForSale(artCollectible)
            } catch (ex: Exception) {
                throw FetchItemForSaleException(
                    "An error occurred when trying to fetch item for sale",
                    ex
                )
            }
        }

    @Throws(WithdrawFromSaleException::class)
    override suspend fun withdrawFromSale(tokenId: BigInteger) = withContext(Dispatchers.IO) {
        try {
            val credentials = walletRepository.loadCredentials()
            artMarketplaceBlockchainDataSource.withdrawFromSale(
                tokenId,
                userCredentialsMapper.mapOutToIn(credentials)
            )
        } catch (ex: Exception) {
            throw WithdrawFromSaleException(
                "An error occurred when trying to withdraw item from sale",
                ex
            )
        }
    }

    @Throws(CheckTokenAddedForSaleException::class)
    override suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.isTokenAddedForSale(
                    tokenId,
                    userCredentialsMapper.mapOutToIn(credentials)
                )
            } catch (ex: Exception) {
                throw CheckTokenAddedForSaleException(
                    "An error occurred when checking if token has been added for sale",
                    ex
                )
            }
        }

    @Throws(FetchMarketplaceStatisticsException::class)
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
                throw FetchMarketplaceStatisticsException(
                    "An error occurred when fetching marketplace statistics",
                    ex
                )
            }
        }

    @Throws(BuyItemException::class)
    override suspend fun buyItem(tokenId: BigInteger) {
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.buyItem(
                    tokenId,
                    userCredentialsMapper.mapOutToIn(credentials)
                )
            } catch (ex: Exception) {
                throw BuyItemException("An error occurred when trying to buy an item", ex)
            }
        }
    }

    @Throws(GetMarketItemsByCategoryException::class)
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
            throw GetMarketItemsByCategoryException(
                "An error occurred when trying to get similar market items",
                ex
            )
        }
    }

    private suspend fun mapToArtCollectibleForSale(item: ArtCollectibleForSaleDTO): ArtCollectibleForSale =
        withContext(Dispatchers.Default) {
            with(item) {
                val tokenDeferred = async {
                    artCollectibleRepository.getTokenById(tokenId)
                }
                val ownerDeferred = async {
                    runCatching {
                        userRepository.getByAddress(owner)
                    }.getOrNull()
                }
                val sellerDeferred = async { userRepository.getByAddress(seller) }
                ArtCollectibleForSale(
                    marketItemId = marketItemId,
                    token = tokenDeferred.await(),
                    seller = sellerDeferred.await(),
                    owner = ownerDeferred.await(),
                    price = getArtCollectiblePrices(this),
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
            val tokensData =
                artCollectibleRepository.getTokens(items.map(ArtCollectibleForSaleDTO::tokenId))
            items.asSequence().map { itemForSale ->
                async {
                    with(itemForSale) {
                        tokensData.find { it.id == tokenId }?.let { token ->
                            val owner = runCatching {
                                userRepository.getByAddress(owner)
                            }.getOrNull()
                            val seller = userRepository.getByAddress(seller)
                            ArtCollectibleForSale(
                                marketItemId = marketItemId,
                                token = token,
                                seller = seller,
                                owner = owner,
                                price = getArtCollectiblePrices(this),
                                sold = sold,
                                canceled = canceled,
                                putForSaleAt = putForSaleAt,
                                soldAt = soldAt,
                                canceledAt = canceledAt
                            )
                        }
                    }
                }
            }.toList().awaitAll().filterNotNull()
        }


    private suspend fun getArtCollectiblePrices(itemForSale: ArtCollectibleForSaleDTO) =  with(itemForSale) {
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