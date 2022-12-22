package com.dreamsoftware.artcollectibles.data.api.impl

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.entity.ArtCollectibleForSaleEntity
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository
): IArtMarketplaceRepository {

    override suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchAvailableMarketItems()
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchSellingMarketItems()
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchOwnedMarketItems()
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchMarketHistory()
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    private suspend fun mapToArtCollectibleForSale(items: Iterable<ArtCollectibleForSaleEntity>): Iterable<ArtCollectibleForSale> =
        items.map {
            val token = artCollectibleRepository.getTokenById(it.tokenId)
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