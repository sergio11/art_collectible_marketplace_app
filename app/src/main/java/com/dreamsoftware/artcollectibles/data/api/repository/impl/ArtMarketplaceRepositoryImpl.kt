package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserInfoMapper
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleForSaleDTO
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IUsersDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

/**
 * Art Marketplace Repository Impl
 * @param artMarketplaceBlockchainDataSource
 * @param artCollectibleRepository
 * @param userDataSource
 * @param userInfoMapper
 * @param walletRepository
 * @param userCredentialsMapper
 */
internal class ArtMarketplaceRepositoryImpl(
    private val artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val userDataSource: IUsersDataSource,
    private val userInfoMapper: UserInfoMapper,
    private val walletRepository: IWalletRepository,
    private val userCredentialsMapper: UserCredentialsMapper
): IArtMarketplaceRepository {

    override suspend fun fetchAvailableMarketItems(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchAvailableMarketItems(userCredentialsMapper.mapOutToIn(credentials))
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun fetchSellingMarketItems(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchSellingMarketItems(userCredentialsMapper.mapOutToIn(credentials))
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun fetchOwnedMarketItems(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchOwnedMarketItems(userCredentialsMapper.mapOutToIn(credentials))
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun fetchMarketHistory(): Iterable<ArtCollectibleForSale> = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        val artCollectibleForSaleList = artMarketplaceBlockchainDataSource.fetchMarketHistory(userCredentialsMapper.mapOutToIn(credentials))
        mapToArtCollectibleForSale(artCollectibleForSaleList)
    }

    override suspend fun putItemForSale(tokenId: BigInteger, price: BigInteger) = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        artMarketplaceBlockchainDataSource.putItemForSale(tokenId, price, userCredentialsMapper.mapOutToIn(credentials))
    }

    override suspend fun withdrawFromSale(tokenId: BigInteger) = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        artMarketplaceBlockchainDataSource.withdrawFromSale(tokenId, userCredentialsMapper.mapOutToIn(credentials))
    }

    override suspend fun isTokenAddedForSale(tokenId: BigInteger): Boolean = withContext(Dispatchers.IO) {
        val credentials = walletRepository.loadCredentials()
        artMarketplaceBlockchainDataSource.isTokenAddedForSale(tokenId, userCredentialsMapper.mapOutToIn(credentials))
    }

    private suspend fun mapToArtCollectibleForSale(items: Iterable<ArtCollectibleForSaleDTO>): Iterable<ArtCollectibleForSale> =
        items.map {
            val token = artCollectibleRepository.getTokenById(it.tokenId)
            val owner = userInfoMapper.mapInToOut(userDataSource.getByAddress(it.owner))
            val seller = userInfoMapper.mapInToOut(userDataSource.getByAddress(it.seller))
            ArtCollectibleForSale(
                marketItemId = it.marketItemId,
                token = token,
                seller = seller,
                owner = owner,
                price = it.price,
                sold = it.sold,
                canceled = it.canceled
            )
        }
}