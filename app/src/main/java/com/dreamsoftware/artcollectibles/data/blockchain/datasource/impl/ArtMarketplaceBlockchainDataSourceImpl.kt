package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleAddedForSaleEventResponse
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtMarketplaceContract.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.tx.FastRawTransactionManager
import java.math.BigInteger

internal class ArtMarketplaceBlockchainDataSourceImpl(
    private val artMarketplaceMapper: ArtMarketplaceMapper,
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j,
) : IArtMarketplaceBlockchainDataSource {

    private enum class MarketItemType { AVAILABLE, SELLING, OWNED, HISTORY }

    override suspend fun fetchAvailableMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.AVAILABLE, credentials = credentials)
        }

    override suspend fun fetchSellingMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.SELLING, credentials = credentials)
        }

    override suspend fun fetchOwnedMarketItems(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.OWNED, credentials = credentials)
        }

    override suspend fun fetchMarketHistory(credentials: Credentials): Iterable<ArtCollectibleForSaleDTO> =
        withContext(Dispatchers.IO) {
            fetchMarketItemsBy(type = MarketItemType.HISTORY, credentials = credentials)
        }

    override suspend fun putItemForSale(
        tokenId: BigInteger,
        price: BigInteger,
        credentials: Credentials
    ): BigInteger =
        withContext(Dispatchers.IO) {
            with(loadContract(credentials)) {
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

    override suspend fun withdrawFromSale(tokenId: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            loadContract(credentials).withdrawFromSale(tokenId).send()
        }
    }

    override suspend fun buyItem(tokenId: BigInteger, price: BigInteger, credentials: Credentials) {
        withContext(Dispatchers.IO) {
            loadContract(credentials).buyItem(tokenId, price).send()
        }
    }

    override suspend fun isTokenAddedForSale(tokenId: BigInteger, credentials: Credentials): Boolean {
        withContext(Dispatchers.IO) {
            TODO("")
        }
    }

    private fun fetchMarketItemsBy(type: MarketItemType, credentials: Credentials) = with(loadContract(credentials)) {
        val marketItems = when (type) {
            MarketItemType.AVAILABLE -> fetchAvailableMarketItems()
            MarketItemType.SELLING -> fetchSellingMarketItems()
            MarketItemType.OWNED -> fetchOwnedMarketItems()
            MarketItemType.HISTORY -> fetchMarketHistory()
        }.send().filterIsInstance<ArtCollectibleForSale>()
        artMarketplaceMapper.mapInListToOutList(marketItems)
    }

    private fun loadContract(credentials: Credentials): ArtMarketplaceContract = with(blockchainConfig) {
        val txManager = FastRawTransactionManager(web3j, credentials, chainId)
        ArtMarketplaceContract.load(
            artCollectibleContractAddress,
            web3j,
            txManager,
            gasProvider
        )
    }
}