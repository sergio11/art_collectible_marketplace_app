package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectibleMintedEventResponse
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.core.SupportBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.ReadonlyTransactionManager
import java.math.BigInteger

/**
 * Art Collectible Blockchain Data Source Impl
 * @param artCollectibleMapper
 * @param blockchainConfig
 * @param web3j
 */
internal class ArtCollectibleBlockchainDataSourceImpl(
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j
) : SupportBlockchainDataSource(blockchainConfig, web3j), IArtCollectibleBlockchainDataSource {

    override suspend fun mintToken(
        metadataCid: String,
        royalty: Long,
        credentials: Credentials
    ): BigInteger =
        withContext(Dispatchers.IO) {
            with(loadContract(credentials)) {
                sendTransaction(
                    encodeFunctionCall = mintToken(metadataCid, BigInteger.valueOf(royalty))
                        .encodeFunctionCall(),
                    credentials = credentials,
                    target = blockchainConfig.artCollectibleContractAddress
                )
                artCollectibleMintedEventFlowable(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST
                )
                    .firstOrError()
                    .map(ArtCollectibleMintedEventResponse::tokenId)
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
            val collectibles =
                loadContract(credentials, readOnlyMode = true).tokensCreatedByMe.send()
                    .filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        }

    override suspend fun getTokensCreatedBy(
        credentials: Credentials,
        creatorAddress: String
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        val collectibles =
            loadContract(credentials, readOnlyMode = true).getTokensCreatedBy(creatorAddress).send()
                .filterIsInstance<ArtCollectible>()
        artCollectibleMapper.mapInListToOutList(collectibles)
    }

    override suspend fun getTokensOwned(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO> =
        withContext(Dispatchers.IO) {
            val collectibles =
                loadContract(credentials, readOnlyMode = true).tokensOwnedByMe.send()
                    .filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        }

    override suspend fun getTokensOwnedBy(
        credentials: Credentials,
        ownerAddress: String
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        val collectibles =
            loadContract(credentials, readOnlyMode = true).getTokensOwnedBy(ownerAddress).send()
                .filterIsInstance<ArtCollectible>()
        artCollectibleMapper.mapInListToOutList(collectibles)
    }

    override suspend fun getTokenById(
        tokenId: BigInteger,
        credentials: Credentials
    ): ArtCollectibleBlockchainDTO =
        withContext(Dispatchers.IO) {
            val collectible =
                loadContract(credentials, readOnlyMode = true).getTokenById(tokenId).send()
            artCollectibleMapper.mapInToOut(collectible)
        }


    private fun loadContract(
        credentials: Credentials,
        readOnlyMode: Boolean = false
    ): ArtCollectibleContract =
        with(blockchainConfig) {
            if (readOnlyMode) {
                ArtCollectibleContract.load(
                    artCollectibleContractAddress,
                    web3j,
                    ReadonlyTransactionManager(web3j, credentials.address),
                    gasProvider
                )
            } else {
                ArtCollectibleContract.load(
                    artCollectibleContractAddress, web3j, credentials, gasProvider
                )
            }
        }
}