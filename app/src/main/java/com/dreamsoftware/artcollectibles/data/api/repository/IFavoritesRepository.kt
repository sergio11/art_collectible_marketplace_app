package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import java.math.BigInteger

interface IFavoritesRepository {

    @Throws(AddToFavoritesDataException::class)
    suspend fun add(tokenId: BigInteger, userAddress: String)

    @Throws(RemoveFromFavoritesDataException::class)
    suspend fun remove(tokenId: BigInteger, userAddress: String)

    @Throws(GetMyFavoriteTokensDataException::class)
    suspend fun getMyFavoriteTokens(): Iterable<ArtCollectible>

    @Throws(GetMostLikedTokensDataException::class)
    suspend fun getMostLikedTokens(limit: Int): Iterable<ArtCollectible>

    @Throws(GetUserLikesByTokenDataException::class)
    suspend fun getUserLikesByToken(tokenId: BigInteger): Iterable<UserInfo>
}