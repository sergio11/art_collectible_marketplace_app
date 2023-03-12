package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.AddToFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetMoreLikedTokensException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RemoveFromFavoritesException

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
}