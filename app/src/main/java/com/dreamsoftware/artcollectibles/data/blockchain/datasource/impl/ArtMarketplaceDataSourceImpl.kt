package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager

class ArtMarketplaceDataSourceImpl(
    private val artMarketplaceMapper: ArtMarketplaceMapper,
    private val blockchainConfig: BlockchainConfig,
    private val walletDataSource: IWalletDataSource,
    private val web3j: Web3j,
): IArtMarketplaceDataSource {

    override suspend fun fetchAvailableMarketItems(): Flow<ArtCollectibleForSaleEntity> = withContext(Dispatchers.IO) {
        loadContract().fetchAvailableMarketItems().send()
            .filterIsInstance<ArtCollectibleForSale>()
            .map { artMarketplaceMapper.mapInToOut(it) }
            .asFlow()
    }

    override suspend fun fetchSellingMarketItems(): Flow<ArtCollectibleForSaleEntity> = withContext(Dispatchers.IO) {
        loadContract().fetchSellingMarketItems().send()
            .filterIsInstance<ArtCollectibleForSale>()
            .map { artMarketplaceMapper.mapInToOut(it) }
            .asFlow()
    }

    override suspend fun fetchOwnedMarketItems(): Flow<ArtCollectibleForSaleEntity> = withContext(Dispatchers.IO) {
        loadContract().fetchOwnedMarketItems().send()
            .filterIsInstance<ArtCollectibleForSale>()
            .map { artMarketplaceMapper.mapInToOut(it) }
            .asFlow()
    }

    private fun loadContract(): ArtMarketplaceContract {
        val txManager = FastRawTransactionManager(web3j, null, blockchainConfig.chainId);
        return ArtMarketplaceContract.load(blockchainConfig.artCollectibleContractAddress,
            web3j, txManager, blockchainConfig.gasProvider);
    }
}