package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import java.math.BigInteger

interface IFavoritesDataSource {

    @Throws(GetFavoritesException::class)
    suspend fun hasAdded(tokenId: BigInteger, userAddress: String): Boolean

    @Throws(GetFavoritesException::class)
    suspend fun getList(userAddress: String): List<String>

    @Throws(GetFavoritesException::class)
    suspend fun tokenCount(tokenId: BigInteger): Long

    @Throws(AddToFavoritesException::class)
    suspend fun add(tokenId: BigInteger, userAddress: String)

    @Throws(RemoveFromFavoritesException::class)
    suspend fun remove(tokenId: BigInteger, userAddress: String)

    @Throws(GetMostLikedTokensException::class)
    suspend fun getMostLikedTokens(limit: Int): List<String>

    @Throws(GetUserLikesByTokenException::class)
    suspend fun getUserLikesByToken(tokenId: BigInteger): List<String>
}