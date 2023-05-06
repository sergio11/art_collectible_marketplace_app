package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectibleMintedEventResponse
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.core.SupportBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.*
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMintedEventMapper
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.TokenStatisticsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleMintedEventDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.TokenStatisticsDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.ReadonlyTransactionManager
import java.math.BigInteger

/**
 * Art Collectible Blockchain Data Source Impl
 * @param artCollectibleMapper
 * @param artCollectibleMintedEventMapper
 * @param tokenStatisticsMapper
 * @param blockchainConfig
 * @param web3j
 */
internal class ArtCollectibleBlockchainDataSourceImpl(
    private val artCollectibleMapper: ArtCollectibleMapper,
    private val artCollectibleMintedEventMapper: ArtCollectibleMintedEventMapper,
    private val tokenStatisticsMapper: TokenStatisticsMapper,
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j
) : SupportBlockchainDataSource(blockchainConfig, web3j), IArtCollectibleBlockchainDataSource {

    override suspend fun observeArtCollectibleMintedEvents(credentials: Credentials): Flow<ArtCollectibleMintedEventDTO> =
        withContext(Dispatchers.IO) {
            with(loadContract(credentials)) {
                artCollectibleMintedEventFlowable(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST
                ).map(artCollectibleMintedEventMapper::mapInToOut).asFlow()
            }
        }

    @Throws(MintTokenException::class)
    override suspend fun mintToken(
        metadataCid: String,
        royalty: Long,
        credentials: Credentials
    ): BigInteger =
        withContext(Dispatchers.IO) {
            try {
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
            } catch (ex: Exception) {
                throw MintTokenException("An error occurred when trying to mint a new token", ex)
            }
        }

    @Throws(BurnTokenException::class)
    override suspend fun burnToken(tokenId: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            try {
                loadContract(credentials).burn(tokenId).send()
            } catch (ex: Exception) {
                throw BurnTokenException("An error occurred when trying to burn a token", ex)
            }
        }
    }

    @Throws(GetTokensCreatedException::class)
    override suspend fun getTokensCreated(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO> =
        withContext(Dispatchers.IO) {
            try {
                val collectibles =
                    loadContract(credentials, readOnlyMode = true).tokensCreatedByMe.send()
                        .filterIsInstance<ArtCollectible>()
                artCollectibleMapper.mapInListToOutList(collectibles)
            } catch (ex: Exception) {
                throw GetTokensCreatedException("An error occurred when trying to get tokens created by address", ex)
            }
        }

    @Throws(GetTokensCreatedException::class)
    override suspend fun getTokensCreatedBy(
        credentials: Credentials,
        creatorAddress: String
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        try {
            val collectibles =
                loadContract(credentials, readOnlyMode = true).getTokensCreatedBy(creatorAddress)
                    .send()
                    .filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        } catch (ex: Exception) {
            throw GetTokensCreatedException("An error occurred when trying to get tokens created by address", ex)
        }
    }

    @Throws(GetTokensCreatedException::class)
    override suspend fun getTokensCreatedBy(
        credentials: Credentials,
        creatorAddress: String,
        limit: Long
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        try {
            val collectibles =
                loadContract(credentials, readOnlyMode = true).getPaginatedTokensCreatedBy(creatorAddress, BigInteger.valueOf(limit)).send()
                    .filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        } catch (ex: Exception) {
            throw GetTokensCreatedException("An error occurred when trying to get tokens created by address", ex)
        }
    }

    @Throws(GetTokensOwnedException::class)
    override suspend fun getTokensOwned(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO> =
        withContext(Dispatchers.IO) {
            try {
                val collectibles =
                    loadContract(credentials, readOnlyMode = true).tokensOwnedByMe.send()
                        .filterIsInstance<ArtCollectible>()
                artCollectibleMapper.mapInListToOutList(collectibles)
            } catch (ex: Exception) {
                throw GetTokensOwnedException("An error occurred when trying to get tokens owned by address", ex)
            }
        }

    @Throws(GetTokensOwnedException::class)
    override suspend fun getTokensOwnedBy(
        credentials: Credentials,
        ownerAddress: String
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        try {
            val collectibles =
                loadContract(credentials, readOnlyMode = true).getTokensOwnedBy(ownerAddress).send()
                    .filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        } catch (ex: Exception) {
            throw GetTokensOwnedException("An error occurred when trying to get tokens owned by address", ex)
        }
    }

    @Throws(GetTokensOwnedException::class)
    override suspend fun getTokensOwnedBy(
        credentials: Credentials,
        ownerAddress: String,
        limit: Long
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        try {
            val collectibles =
                loadContract(credentials, readOnlyMode = true).getPaginatedTokensOwnedBy(ownerAddress, BigInteger.valueOf(limit)).send()
                    .filterIsInstance<ArtCollectible>()
            artCollectibleMapper.mapInListToOutList(collectibles)
        } catch (ex: Exception) {
            throw GetTokensOwnedException("An error occurred when trying to get tokens owned by address", ex)
        }
    }

    @Throws(GetTokenByIdException::class)
    override suspend fun getTokenById(
        tokenId: BigInteger,
        credentials: Credentials
    ): ArtCollectibleBlockchainDTO =
        withContext(Dispatchers.IO) {
            try {
                val collectible =
                    loadContract(credentials, readOnlyMode = true).getTokenById(tokenId).send()
                artCollectibleMapper.mapInToOut(collectible)
            } catch (ex: Exception) {
                throw GetTokenByIdException("An error occurred when trying to get token by id", ex)
            }
        }

    @Throws(GetTokenByCidException::class)
    override suspend fun getTokenByCID(
        cid: String,
        credentials: Credentials
    ): ArtCollectibleBlockchainDTO = withContext(Dispatchers.IO) {
        try {
            val collectible =
                loadContract(credentials, readOnlyMode = true).getTokenByMetadataCid(cid).send()
            artCollectibleMapper.mapInToOut(collectible)
        } catch (ex: Exception) {
            throw GetTokenByCidException("An error occurred when trying to get token by CID", ex)
        }
    }

    @Throws(GetTokenByIdException::class)
    override suspend fun getTokens(
        tokenList: Iterable<BigInteger>,
        credentials: Credentials
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        try {
            val collectibleList =
                loadContract(credentials, readOnlyMode = true).getTokens(tokenList.toList()).send() as List<ArtCollectible>
            artCollectibleMapper.mapInListToOutList(collectibleList)
        } catch (ex: Exception) {
            throw GetTokenByIdException("An error occurred when trying to get token list by ID", ex)
        }
    }

    @Throws(GetTokenByCidException::class)
    override suspend fun getTokensByCID(
        cidList: Iterable<String>,
        credentials: Credentials
    ): Iterable<ArtCollectibleBlockchainDTO> = withContext(Dispatchers.IO) {
        try {
            val collectibleList = loadContract(credentials, readOnlyMode = true)
                .getTokensByMetadataCids(cidList.toList())
                .send() as List<ArtCollectible>
            artCollectibleMapper.mapInListToOutList(collectibleList)
        } catch (ex: Exception) {
            throw GetTokenByCidException("An error occurred when trying to get token list by CID", ex)
        }
    }

    @Throws(FetchTokensStatisticsException::class)
    override suspend fun fetchTokensStatisticsByAddress(credentials: Credentials, targetAddress: String): TokenStatisticsDTO = withContext(Dispatchers.IO) {
        try {
            val tokensStatistics = loadContract(credentials, readOnlyMode = true)
                .fetchTokensStatisticsByAddress(targetAddress).send()
            tokenStatisticsMapper.mapInToOut(tokensStatistics)
        } catch (ex: Exception) {
            throw FetchTokensStatisticsException("An error occurred when trying to fetch tokens statistics", ex)
        }
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