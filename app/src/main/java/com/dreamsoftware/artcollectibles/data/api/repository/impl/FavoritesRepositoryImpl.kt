package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.AddToFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RemoveFromFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Favorites Repository Impl
 * @param favoritesDataSource
 */
internal class FavoritesRepositoryImpl(
    private val favoritesDataSource: IFavoritesDataSource
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
}