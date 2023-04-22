package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
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
 * @param userRepository
 * @param artCollectibleMemoryCacheDataSource
 */
internal class FavoritesRepositoryImpl(
    private val favoritesDataSource: IFavoritesDataSource,
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val walletRepository: IWalletRepository,
    private val userRepository: IUserRepository,
    private val artCollectibleMemoryCacheDataSource: IArtCollectibleMemoryCacheDataSource
): IFavoritesRepository {

    @Throws(AddToFavoritesDataException::class)
    override suspend fun add(tokenId: BigInteger, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.add(tokenId, userAddress).also {
                    with(artCollectibleMemoryCacheDataSource) {
                        if(hasKey(tokenId)) {
                            findByKey(tokenId).let {
                                it.copy(favoritesCount = it.favoritesCount + 1)
                            }.let {
                                save(it.id, it)
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw AddToFavoritesDataException("An error occurred when trying to add to favorites", ex)
            }
        }
    }

    @Throws(RemoveFromFavoritesDataException::class)
    override suspend fun remove(tokenId: BigInteger, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.remove(tokenId, userAddress).also {
                    with(artCollectibleMemoryCacheDataSource) {
                        if(hasKey(tokenId)) {
                            findByKey(tokenId).let {
                                it.copy(favoritesCount = it.favoritesCount - 1)
                            }.let {
                                save(it.id, it)
                            }
                        }
                    }
                }
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

    @Throws(GetMostLikedTokensDataException::class)
    override suspend fun getMostLikedTokens(limit: Int): Iterable<ArtCollectible> =
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.getMostLikedTokens(limit).map {
                    async { artCollectibleRepository.getTokenById(BigInteger(it)) }
                }.awaitAll()
            } catch (ex: Exception) {
                throw GetMostLikedTokensDataException("An error occurred when trying to get more liked tokens", ex)
            }
        }

    @Throws(GetUserLikesByTokenDataException::class)
    override suspend fun getUserLikesByToken(tokenId: BigInteger): Iterable<UserInfo> =
        withContext(Dispatchers.IO) {
            try {
                favoritesDataSource.getUserLikesByToken(tokenId).asSequence().map { userAddress ->
                    async { runCatching { userRepository.getByAddress(userAddress, true) }.getOrNull() }
                }.toList().awaitAll().filterNotNull()
            } catch (ex: Exception) {
                throw GetUserLikesByTokenDataException("An error occurred when trying to get user likes by token", ex)
            }
        }

}