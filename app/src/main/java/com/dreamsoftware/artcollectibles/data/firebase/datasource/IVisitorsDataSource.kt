package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.AddVisitorException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetVisitorException

interface IVisitorsDataSource {

    @Throws(AddVisitorException::class)
    suspend fun addVisitor(tokenId: String, userAddress: String)

    @Throws(GetVisitorException::class)
    suspend fun count(tokenId: String): Long
}