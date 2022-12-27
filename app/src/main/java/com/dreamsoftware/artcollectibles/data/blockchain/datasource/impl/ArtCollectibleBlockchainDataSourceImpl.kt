package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.tx.FastRawTransactionManager
import java.math.BigInteger

internal class ArtCollectibleBlockchainDataSourceImpl(
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j
) : IArtCollectibleBlockchainDataSource {

    override suspend fun mintToken(metadataCid: String, royalty: Long, credentials: Credentials): BigInteger =
        withContext(Dispatchers.IO) {
            with(loadContract(credentials)) {
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

    override suspend fun burnToken(tokenId: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            loadContract(credentials).burn(tokenId).send()
        }
    }

    override suspend fun getTokensCreated(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO> =
        withContext(Dispatchers.IO) {
            val collectibles = loadContract(credentials).tokensCreatedByMe.send().filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        }

    override suspend fun getTokensOwned(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO> =
        withContext(Dispatchers.IO) {
            val collectibles = loadContract(credentials).tokensOwnedByMe.send().filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        }

    override suspend fun getTokenById(tokenId: BigInteger, credentials: Credentials): ArtCollectibleBlockchainDTO =
        withContext(Dispatchers.IO) {
            val collectible = loadContract(credentials).getTokenById(tokenId).send()
            artCollectibleMapper.mapInToOut(collectible)
        }

    private fun loadContract(credentials: Credentials): ArtCollectibleContract = with(blockchainConfig) {
        val txManager = FastRawTransactionManager(web3j, credentials, chainId)
        ArtCollectibleContract.load(
            artCollectibleContractAddress, web3j, txManager, gasProvider
        )
    }
}