package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.MarketplaceStatisticsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

/**
 * Art Marketplace Repository Impl
 * @param artMarketplaceBlockchainDataSource
 * @param artCollectibleRepository
 * @param userDataSource
 * @param userInfoMapper
 * @param walletRepository
 * @param userCredentialsMapper
 * @param marketplaceStatisticsMapper
 */
internal class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val userDataSource: IUsersDataSource,
    private val userInfoMapper: UserInfoMapper,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val marketplaceStatisticsMapper: MarketplaceStatisticsMapper
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

    @Throws(PutItemForSaleException::class)
    override suspend fun putItemForSale(tokenId: BigInteger, price: Float) =
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.putItemForSale(
                    tokenId,
                    price,
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
    override suspend fun buyItem(tokenId: BigInteger, price: BigInteger) {
        withContext(Dispatchers.IO) {
            try {
                val credentials = walletRepository.loadCredentials()
                artMarketplaceBlockchainDataSource.buyItem(
                    tokenId,
                    price,
                    userCredentialsMapper.mapOutToIn(credentials)
                )
            } catch (ex: Exception) {
                throw BuyItemException("An error occurred when trying to buy an item", ex)
            }
        }
    }

    private suspend fun mapToArtCollectibleForSaleList(items: Iterable<ArtCollectibleForSaleDTO>): Iterable<ArtCollectibleForSale> =
        items.map { mapToArtCollectibleForSale(it) }

    private suspend fun mapToArtCollectibleForSale(item: ArtCollectibleForSaleDTO): ArtCollectibleForSale =
        with(item) {
            val token = artCollectibleRepository.getTokenById(tokenId)
            val owner =
                kotlin.runCatching { userInfoMapper.mapInToOut(userDataSource.getByAddress(owner)) }
                    .getOrNull()
            val seller = userInfoMapper.mapInToOut(userDataSource.getByAddress(seller))
            ArtCollectibleForSale(
                marketItemId = marketItemId,
                token = token,
                seller = seller,
                owner = owner,
                price = price,
                sold = sold,
                canceled = canceled
            )
        }
}