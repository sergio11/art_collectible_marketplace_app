package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.FetchMarketStatisticsException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RegisterEventException

interface IStatisticsDataSource {

    @Throws(FetchMarketStatisticsException::class)
    suspend fun fetchUsersWithMorePurchases(limit: Int): Iterable<String>

    @Throws(FetchMarketStatisticsException::class)
    suspend fun fetchUsersWithMoreSales(limit: Int): Iterable<String>

    @Throws(FetchMarketStatisticsException::class)
    suspend fun fetchUsersWithMoreTokensCreated(limit: Int): Iterable<String>

    @Throws(RegisterEventException::class)
    suspend fun registerNewPurchase(userUid: String)

    @Throws(RegisterEventException::class)
    suspend fun registerNewSale(userUid: String)

    @Throws(RegisterEventException::class)
    suspend fun registerNewCreation(userUid: String)
}