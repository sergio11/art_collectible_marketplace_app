package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.tx.FastRawTransactionManager
import java.math.BigInteger

internal class ArtCollectibleBlockchainDataSourceImpl(
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val blockchainConfig: BlockchainConfig,
    private val preferencesDataSource: IPreferencesDataSource,
    private val walletDataSource: IWalletDataSource,
    private val web3j: Web3j
) : IArtCollectibleBlockchainDataSource {

    override suspend fun mintToken(metadataCid: String, royalty: Long): Flow<BigInteger> =
        withContext(Dispatchers.IO) {
            loadContract()
                .map {
                    it.mintToken(metadataCid, BigInteger.valueOf(royalty)).send()
                    it.artCollectibleMintedEventFlowable(
                        EthFilter(
                            DefaultBlockParameterName.LATEST,
                            DefaultBlockParameterName.LATEST,
                            blockchainConfig.artCollectibleContractAddress
                        )
                    )
                        .firstOrError()
                        .map(ArtCollectibleContract.ArtCollectibleMintedEventResponse::tokenId)
                        .blockingGet()
                }
        }

    @OptIn(FlowPreview::class)
    override suspend fun getTokensCreated(): Flow<ArtCollectibleBlockchainEntity> =
        withContext(Dispatchers.IO) {
            loadContract()
                .flatMapConcat {
                    it.tokensCreatedByMe.send()
                        .filterIsInstance<ArtCollectible>().asFlow()
                }
                .map { artCollectibleMapper.mapInToOut(it) }
        }

    @OptIn(FlowPreview::class)
    override suspend fun getTokensOwned(): Flow<ArtCollectibleBlockchainEntity> =
        withContext(Dispatchers.IO) {
            loadContract()
                .flatMapConcat {
                    it.tokensOwnedByMe.send().filterIsInstance<ArtCollectible>().asFlow()
                }
                .map { artCollectibleMapper.mapInToOut(it) }
        }

    private suspend fun loadContract(): Flow<ArtCollectibleContract> = with(blockchainConfig) {
        preferencesDataSource.getWalletPassword()
            .map { walletPassword -> walletDataSource.loadCredentials(walletPassword) }
            .map { credentials -> FastRawTransactionManager(web3j, credentials, chainId) }
            .map { txManager ->
                ArtCollectibleContract.load(
                    artCollectibleContractAddress,
                    web3j,
                    txManager,
                    gasProvider
                )
            }
    }

}