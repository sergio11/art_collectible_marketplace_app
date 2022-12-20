package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager

class ArtCollectibleDataSourceImpl(
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val blockchainConfig: BlockchainConfig,
    private val preferencesDataSource: IPreferencesDataSource,
    private val walletDataSource: IWalletDataSource,
    private val web3j: Web3j
): IArtCollectibleDataSource {

    override suspend fun getTokensCreated(): Flow<ArtCollectibleBlockchainEntity> = withContext(Dispatchers.IO) {
        loadContract().tokensCreatedByMe.send()
            .filterIsInstance<ArtCollectible>()
            .map { artCollectibleMapper.mapInToOut(it) }
            .asFlow()
    }

    override suspend fun getTokensOwned(): Flow<ArtCollectibleBlockchainEntity> = withContext(Dispatchers.IO) {
        loadContract().tokensOwnedByMe.send()
            .filterIsInstance<ArtCollectible>()
            .map { artCollectibleMapper.mapInToOut(it) }
            .asFlow()
    }

    private fun loadContract(): ArtCollectibleContract {
        val txManager = FastRawTransactionManager(web3j, null, blockchainConfig.chainId);
        return ArtCollectibleContract.load(blockchainConfig.artCollectibleContractAddress,
            web3j, txManager, blockchainConfig.gasProvider);
    }

}