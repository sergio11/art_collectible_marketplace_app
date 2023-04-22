package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.GetMostVisitedTokensDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetVisitorsByTokenDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterVisitorDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import java.math.BigInteger

interface IVisitorsRepository {

    @Throws(RegisterVisitorDataException::class)
    suspend fun register(tokenId: BigInteger, userAddress: String)

    @Throws(GetVisitorsByTokenDataException::class)
    suspend fun getByTokenId(tokenId: BigInteger): Iterable<UserInfo>

    @Throws(GetMostVisitedTokensDataException::class)
    suspend fun getMostVisitedTokens(limit: Int): Iterable<ArtCollectible>

}