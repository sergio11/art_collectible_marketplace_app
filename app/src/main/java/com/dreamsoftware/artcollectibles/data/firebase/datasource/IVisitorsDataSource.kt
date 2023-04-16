package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.AddVisitorException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetVisitorException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetVisitorsByTokenException
import java.math.BigInteger

interface IVisitorsDataSource {

    @Throws(AddVisitorException::class)
    suspend fun addVisitor(tokenId: BigInteger, userAddress: String)

    @Throws(GetVisitorException::class)
    suspend fun count(tokenId: BigInteger): Long

    @Throws(GetVisitorsByTokenException::class)
    suspend fun getByTokenId(tokenId: BigInteger): List<String>
}