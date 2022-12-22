package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainEntity
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.tx.FastRawTransactionManager
import java.math.BigInteger

internal class ArtCollectibleBlockchainDataSourceImpl(
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val blockchainConfig: BlockchainConfig,
    private val walletDataSource: IWalletDataSource,
    private val web3j: Web3j
) : IArtCollectibleBlockchainDataSource {

    override suspend fun mintToken(metadataCid: String, royalty: Long): BigInteger =
        withContext(Dispatchers.IO) {
            with(loadContract()) {
                mintToken(metadataCid, BigInteger.valueOf(royalty)).send()
                artCollectibleMintedEventFlowable(
                    EthFilter(
                        DefaultBlockParameterName.LATEST,
                        DefaultBlockParameterName.LATEST,
                        blockchainConfig.artCollectibleContractAddress
                    )
                ).firstOrError()
                    .map(ArtCollectibleContract.ArtCollectibleMintedEventResponse::tokenId)
                    .blockingGet()
            }
        }

    override suspend fun getTokensCreated(): Iterable<ArtCollectibleBlockchainEntity> =
        withContext(Dispatchers.IO) {
            val collectibles = loadContract().tokensCreatedByMe.send().filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        }

    override suspend fun getTokensOwned(): Iterable<ArtCollectibleBlockchainEntity> =
        withContext(Dispatchers.IO) {
            val collectibles = loadContract().tokensOwnedByMe.send().filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        }

    override suspend fun getTokenById(tokenId: BigInteger): ArtCollectibleBlockchainEntity =
        withContext(Dispatchers.IO) {
            val collectible = loadContract().getTokenById(tokenId).send()
            artCollectibleMapper.mapInToOut(collectible)
        }

    private suspend fun loadContract(): ArtCollectibleContract = with(blockchainConfig) {
        val credentials = walletDataSource.loadCredentials()
        val txManager = FastRawTransactionManager(web3j, credentials, chainId)
        ArtCollectibleContract.load(
            artCollectibleContractAddress, web3j, txManager, gasProvider
        )
    }
}