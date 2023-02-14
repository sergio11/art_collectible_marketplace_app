package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.AddToFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetFavoritesException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RemoveFromFavoritesException

interface IFavoritesDataSource {

    @Throws(GetFavoritesException::class)
    suspend fun hasAdded(tokenId: String, userId: String): Boolean

    @Throws(GetFavoritesException::class)
    suspend fun getList(userId: String): List<String>

    @Throws(GetFavoritesException::class)
    suspend fun count(id: String): Long

    @Throws(AddToFavoritesException::class)
    suspend fun add(tokenId: String, userId: String)

    @Throws(RemoveFromFavoritesException::class)
    suspend fun remove(tokenId: String, userId: String)
}