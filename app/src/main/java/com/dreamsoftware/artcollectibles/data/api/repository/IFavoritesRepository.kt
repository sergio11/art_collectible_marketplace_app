package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.AddToFavoritesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetMoreLikedTokensDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RemoveFromFavoritesDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible

interface IFavoritesRepository {

    @Throws(AddToFavoritesDataException::class)
    suspend fun add(tokenId: String, userAddress: String)

    @Throws(RemoveFromFavoritesDataException::class)
    suspend fun remove(tokenId: String, userAddress: String)

    @Throws(GetMoreLikedTokensDataException::class)
    suspend fun getMoreLikedTokens(limit: Long): Iterable<ArtCollectible>
}