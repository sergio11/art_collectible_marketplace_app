package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.MarketStatisticsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.WalletStatisticsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketplaceStatisticsDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.WalletStatisticsDTO
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
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j,
) : IArtMarketplaceBlockchainDataSource {

    private companion object {
        private val DEFAULT_COST_OF_PUTTING_FOR_SALE_IN_ETH = BigDecimal.valueOf(0.010)
    }

    private enum class MarketItemType { AVAILABLE, SELLING, OWNED, HISTORY }

    override suspend fun fetchAvailableMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.AVAILABLE, credentials = credentials)
        }

    override suspend fun fetchSellingMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.SELLING, credentials = credentials)
        }

    override suspend fun fetchOwnedMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.OWNED, credentials = credentials)
        }

    override suspend fun fetchMarketHistory(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.HISTORY, credentials = credentials)
        }

    override suspend fun putItemForSale(
        tokenId: BigInteger,
        price: Float,
        credentials: Credentials
    ): Unit =
        withContext(Dispatchers.IO) {
            with(loadContract(credentials)) {
                val defaultCostOfPuttingForSale =
                    Convert.toWei(DEFAULT_COST_OF_PUTTING_FOR_SALE_IN_ETH, Convert.Unit.ETHER)
                        .toBigInteger()
                val putItemForSalePrice =
                    Convert.toWei(price.toString(), Convert.Unit.ETHER).toBigInteger()
                putItemForSale(tokenId, putItemForSalePrice, defaultCostOfPuttingForSale).send()
            }
        }

    override suspend fun withdrawFromSale(tokenId: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            loadContract(credentials).withdrawFromSale(tokenId).send()
        }
    }

    override suspend fun buyItem(tokenId: BigInteger, price: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            loadContract(credentials).buyItem(tokenId, price).send()
        }
    }

    override suspend fun fetchItemForSale(
        tokenId: BigInteger,
        credentials: Credentials
    ): ArtCollectibleForSaleDTO = withContext(Dispatchers.IO) {
        val itemForSale = loadContract(credentials).fetchItemForSale(tokenId).send()
        artMarketplaceMapper.mapInToOut(itemForSale)
    }

    override suspend fun isTokenAddedForSale(
        tokenId: BigInteger,
        credentials: Credentials
    ): Boolean =
        withContext(Dispatchers.IO) {
            loadContract(credentials).isTokenAddedForSale(tokenId).send()
        }

    override suspend fun fetchMarketplaceStatistics(credentials: Credentials): MarketplaceStatisticsDTO =
        withContext(Dispatchers.IO) {
            marketStatisticsMapper.mapInToOut(loadContract(credentials).fetchMarketStatistics().send())
        }

    override suspend fun fetchWalletStatistics(credentials: Credentials, ownerAddress: String): WalletStatisticsDTO =
        withContext(Dispatchers.IO) {
            walletStatisticsMapper.mapInToOut(loadContract(credentials).fetchWalletStatistics(ownerAddress).send())
        }

    private fun fetchMarketItemsBy(type: MarketItemType, credentials: Credentials) =
        with(loadContract(credentials)) {
            val marketItems = when (type) {
                MarketItemType.AVAILABLE -> fetchAvailableMarketItems()
                MarketItemType.SELLING -> fetchSellingMarketItems()
                MarketItemType.OWNED -> fetchOwnedMarketItems()
                MarketItemType.HISTORY -> fetchMarketHistory()
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