package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.AddToFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetMoreLikedTokensDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetMyFavoriteTokensDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RemoveFromFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.math.BigInteger

/**
 * Favorites Repository Impl
 * @param favoritesDataSource
 * @param artCollectibleRepository
 * @param walletRepository
 */
internal class FavoritesRepositoryImpl(
    private val favoritesDataSource: IFavoritesDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val walletRepository: IWalletRepository
): IFavoritesRepository {

    @Throws(AddToFavoritesDataException::class)
    override suspend fun add(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.add(tokenId, userAddress)
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw AddToFavoritesDataException("An error occurred when trying to add to favorites", ex)
            }
        }
    }

    @Throws(RemoveFromFavoritesDataException::class)
    override suspend fun remove(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.remove(tokenId, userAddress)
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw RemoveFromFavoritesDataException("An error occurred when trying to remove from favorites", ex)
            }
        }
    }

    @Throws(GetMyFavoriteTokensDataException::class)
    override suspend fun getMyFavoriteTokens(): Iterable<ArtCollectible> =
        withContext(Dispatchers.IO) {
            try {
                val authCredentials = walletRepository.loadCredentials()
                favoritesDataSource.getList(authCredentials.address).map {
                    async { artCollectibleRepository.getTokenById(BigInteger(it)) }
                }.awaitAll()
            } catch (ex: Exception) {
                throw GetMyFavoriteTokensDataException("An error occurred when trying to get my favorite tokens", ex)
            }
        }

    @Throws(GetMoreLikedTokensDataException::class)
    override suspend fun getMoreLikedTokens(limit: Long): Iterable<ArtCollectible> =
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.getMoreLikedTokens(limit).map {
                    async { artCollectibleRepository.getTokenById(BigInteger(it)) }
                }.awaitAll()
            } catch (ex: Exception) {
                throw GetMoreLikedTokensDataException("An error ocurred when trying to get more liked tokens")
            }
        }

}