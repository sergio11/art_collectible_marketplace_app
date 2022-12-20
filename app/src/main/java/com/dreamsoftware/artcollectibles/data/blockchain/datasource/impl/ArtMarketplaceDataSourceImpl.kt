package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager

class ArtMarketplaceDataSourceImpl(
    private val artMarketplaceMapper: ArtMarketplaceMapper,
    private val blockchainConfig: BlockchainConfig,
    private val preferencesDataSource: IPreferencesDataSource,
    private val walletDataSource: IWalletDataSource,
    private val web3j: Web3j,
) : IArtMarketplaceDataSource {

    enum class MarketItemType { AVAILABLE, SELLING, OWNED }

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

    @OptIn(FlowPreview::class)
    private suspend fun fetchMarketItemsBy(type: MarketItemType) = loadContract()
        .flatMapConcat {
            when (type) {
                MarketItemType.AVAILABLE -> it.fetchAvailableMarketItems()
                MarketItemType.SELLING -> it.fetchSellingMarketItems()
                MarketItemType.OWNED -> it.fetchOwnedMarketItems()
            }.send().filterIsInstance<ArtCollectibleForSale>().asFlow()
        }
        .map { artMarketplaceMapper.mapInToOut(it) }

    private suspend fun loadContract(): Flow<ArtMarketplaceContract> = with(blockchainConfig) {
        preferencesDataSource.getWalletPassword()
            .map { walletPassword -> walletDataSource.loadCredentials(walletPassword) }
            .map { credentials -> FastRawTransactionManager(web3j, credentials, chainId) }
            .map { txManager -> ArtMarketplaceContract.load(artCollectibleContractAddress, web3j, txManager, gasProvider); }
    }
}