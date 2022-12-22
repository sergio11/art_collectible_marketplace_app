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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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

    @OptIn(FlowPreview::class)
    override suspend fun fetchAvailableMarketItems(): Flow<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.AVAILABLE)
        }

    @OptIn(FlowPreview::class)
    override suspend fun fetchSellingMarketItems(): Flow<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.SELLING)
        }

    override suspend fun fetchOwnedMarketItems(): Flow<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.OWNED)
        }

    override suspend fun fetchMarketHistory(): Flow<ArtCollectibleForSaleEntity> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.HISTORY)
        }

    override suspend fun putItemForSale(tokenId: BigInteger, price: BigInteger): Flow<BigInteger> =
        withContext(Dispatchers.IO) {
            loadContract()
                .map {
                    it.putItemForSale(tokenId, price, it.DEFAULT_COST_OF_PUTTING_FOR_SALE().send())
                    it.artCollectibleAddedForSaleEventFlowable(
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
            loadContract().map {
                it.withdrawFromSale(tokenId).send()
            }
        }
    }

    override suspend fun buyItem(tokenId: BigInteger, price: BigInteger) {
        withContext(Dispatchers.IO) {
            loadContract().map {
                it.buyItem(tokenId, price).send()
            }
        }
    }

    @OptIn(FlowPreview::class)
    private suspend fun fetchMarketItemsBy(type: MarketItemType) = loadContract()
        .flatMapConcat {
            when (type) {
                MarketItemType.AVAILABLE -> it.fetchAvailableMarketItems()
                MarketItemType.SELLING -> it.fetchSellingMarketItems()
                MarketItemType.OWNED -> it.fetchOwnedMarketItems()
                MarketItemType.HISTORY -> it.fetchMarketHistory()
            }.send().filterIsInstance<ArtCollectibleForSale>().asFlow()
        }
        .map { artMarketplaceMapper.mapInToOut(it) }

    private suspend fun loadContract(): Flow<ArtMarketplaceContract> = with(blockchainConfig) {
        walletDataSource.loadCredentials()
            .map { credentials -> FastRawTransactionManager(web3j, credentials, chainId) }
            .map { txManager ->
                ArtMarketplaceContract.load(
                    artCollectibleContractAddress,
                    web3j,
                    txManager,
                    gasProvider
                );
            }
    }
}