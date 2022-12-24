package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val userCredentialsMapper: UserCredentialsMapper
): IArtMarketplaceRepository {

    override suspend fun fetchAvailableMarketItems(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchAvailableMarketItems(userCredentialsMapper.mapOutToIn(userWalletCredentials))
        mapToArtCollectibleForSale(userWalletCredentials, artCollectibleForSaleList)
    }

    override suspend fun fetchSellingMarketItems(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchSellingMarketItems(userCredentialsMapper.mapOutToIn(userWalletCredentials))
        mapToArtCollectibleForSale(userWalletCredentials, artCollectibleForSaleList)
    }

    override suspend fun fetchOwnedMarketItems(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchOwnedMarketItems(userCredentialsMapper.mapOutToIn(userWalletCredentials))
        mapToArtCollectibleForSale(userWalletCredentials, artCollectibleForSaleList)
    }

    override suspend fun fetchMarketHistory(userWalletCredentials: UserWalletCredentials): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchMarketHistory(userCredentialsMapper.mapOutToIn(userWalletCredentials))
        mapToArtCollectibleForSale(userWalletCredentials, artCollectibleForSaleList)
    }

    private suspend fun mapToArtCollectibleForSale(userWalletCredentials: UserWalletCredentials, items: Iterable<ArtCollectibleForSaleEntity>): Iterable<ArtCollectibleForSale> =
        items.map {
            val token = artCollectibleRepository.getTokenById(it.tokenId, userWalletCredentials)
            ArtCollectibleForSale(
                marketItemId = it.marketItemId,
                token = token,
                seller = it.seller,
                owner = it.owner,
                price = it.price,
                sold = it.sold,
                canceled = it.canceled
            )
        }
}