package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.AddToFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RemoveFromFavoritesDataException

interface IFavoritesRepository {

    @Throws(AddToFavoritesDataException::class)
    suspend fun add(tokenId: String, userAddress: String)

    @Throws(RemoveFromFavoritesDataException::class)
    suspend fun remove(tokenId: String, userAddress: String)
}