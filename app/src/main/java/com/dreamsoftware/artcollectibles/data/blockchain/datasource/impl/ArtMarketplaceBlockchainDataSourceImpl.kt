package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleAddedForSaleEventResponse
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.tx.FastRawTransactionManager
import java.math.BigInteger

internal class ArtMarketplaceBlockchainDataSourceImpl(
    private val artMarketplaceMapper: ArtMarketplaceMapper,
    private val blockchainConfig: BlockchainConfig,
    private val walletDataSource: IWalletDataSource,
    private val web3j: Web3j,
) : IArtMarketplaceBlockchainDataSource {

    private enum class MarketItemType { AVAILABLE, SELLING, OWNED, HISTORY }

    override suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.AVAILABLE)
        }

    override suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.SELLING)
        }

    override suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.OWNED)
        }

    override suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.HISTORY)
        }

    override suspend fun putItemForSale(
        tokenId: BigInteger,
        price: BigInteger
    ): BigInteger =
        withContext(Dispatchers.IO) {
            with(loadContract()) {
                putItemForSale(tokenId, price, DEFAULT_COST_OF_PUTTING_FOR_SALE().send())
                artCollectibleAddedForSaleEventFlowable(
                    EthFilter(
                        DefaultBlockParameterName.LATEST,
                        DefaultBlockParameterName.LATEST,
                        blockchainConfig.artMarketplaceContractAddress
                    )
                )
                    .firstOrError()
                    .map(ArtCollectibleAddedForSaleEventResponse::id)
                    .blockingGet()
            }
        }

    override suspend fun withdrawFromSale(tokenId: BigInteger) {
        withContext(Dispatchers.IO) {
            loadContract().withdrawFromSale(tokenId).send()
        }
    }

    override suspend fun buyItem(tokenId: BigInteger, price: BigInteger) {
        withContext(Dispatchers.IO) {
            loadContract().buyItem(tokenId, price).send()
        }
    }

    private suspend fun fetchMarketItemsBy(type: MarketItemType) = with(loadContract()) {
        val marketItems = when (type) {
            MarketItemType.AVAILABLE -> fetchAvailableMarketItems()
            MarketItemType.SELLING -> fetchSellingMarketItems()
            MarketItemType.OWNED -> fetchOwnedMarketItems()
            MarketItemType.HISTORY -> fetchMarketHistory()
        }.send().filterIsInstance<ArtCollectibleForSale>()
        artMarketplaceMapper.mapInListToOutList(marketItems)
    }

    private suspend fun loadContract(): ArtMarketplaceContract = with(blockchainConfig) {
        val credentials = walletDataSource.loadCredentials()
        val txManager = FastRawTransactionManager(web3j, credentials, chainId)
        ArtMarketplaceContract.load(
            artCollectibleContractAddress,
            web3j,
            txManager,
            gasProvider
        )
    }
}