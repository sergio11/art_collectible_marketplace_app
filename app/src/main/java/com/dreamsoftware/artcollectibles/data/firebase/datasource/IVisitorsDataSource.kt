package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import java.math.BigInteger

interface IVisitorsDataSource {

    @Throws(AddVisitorException::class)
    suspend fun addVisitor(tokenId: BigInteger, userAddress: String)

    @Throws(GetVisitorsCountException::class)
    suspend fun count(tokenId: BigInteger): Long

    @Throws(GetVisitorsByTokenException::class)
    suspend fun getByTokenId(tokenId: BigInteger): List<String>

    @Throws(GetMostVisitedTokensException::class)
    suspend fun getMostVisitedTokens(limit: Int): List<String>
}