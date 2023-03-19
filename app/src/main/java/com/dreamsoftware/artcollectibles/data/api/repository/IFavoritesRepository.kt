package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo

interface IFavoritesRepository {

    @Throws(AddToFavoritesDataException::class)
    suspend fun add(tokenId: String, userAddress: String)

    @Throws(RemoveFromFavoritesDataException::class)
    suspend fun remove(tokenId: String, userAddress: String)

    @Throws(GetMyFavoriteTokensDataException::class)
    suspend fun getMyFavoriteTokens(): Iterable<ArtCollectible>

    @Throws(GetMoreLikedTokensDataException::class)
    suspend fun getMoreLikedTokens(limit: Long): Iterable<ArtCollectible>

    @Throws(GetUserLikesByTokenDataException::class)
    suspend fun getUserLikesByToken(tokenId: String): Iterable<UserInfo>
}