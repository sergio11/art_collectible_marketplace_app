package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*

interface IFavoritesDataSource {

    @Throws(GetFavoritesException::class)
    suspend fun hasAdded(tokenId: String, userAddress: String): Boolean

    @Throws(GetFavoritesException::class)
    suspend fun getList(userAddress: String): List<String>

    @Throws(GetFavoritesException::class)
    suspend fun tokenCount(tokenId: String): Long

    @Throws(AddToFavoritesException::class)
    suspend fun add(tokenId: String, userAddress: String)

    @Throws(RemoveFromFavoritesException::class)
    suspend fun remove(tokenId: String, userAddress: String)

    @Throws(GetMoreLikedTokensException::class)
    suspend fun getMoreLikedTokens(limit: Long): List<String>

    @Throws(GetUserLikesByTokenException::class)
    suspend fun getUserLikesByToken(tokenId: String): List<String>
}