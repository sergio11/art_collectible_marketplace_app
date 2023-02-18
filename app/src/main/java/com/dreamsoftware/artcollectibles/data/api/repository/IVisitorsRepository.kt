package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.RegisterVisitorDataException

interface IVisitorsRepository {

    @Throws(RegisterVisitorDataException::class)
    suspend fun register(tokenId: String, userAddress: String)
}