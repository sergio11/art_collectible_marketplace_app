package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.FetchMarketStatisticsException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RegisterEventException
import com.dreamsoftware.artcollectibles.data.firebase.model.MarketStatisticDTO

interface IStatisticsDataSource {

    @Throws(FetchMarketStatisticsException::class)
    suspend fun fetchUsersWithMorePurchases(limit: Int): Iterable<MarketStatisticDTO>

    @Throws(FetchMarketStatisticsException::class)
    suspend fun fetchUsersWithMoreSales(limit: Int): Iterable<MarketStatisticDTO>

    @Throws(FetchMarketStatisticsException::class)
    suspend fun fetchUsersWithMoreTokensCreated(limit: Int): Iterable<MarketStatisticDTO>

    @Throws(RegisterEventException::class)
    suspend fun registerNewPurchase(userUid: String)

    @Throws(RegisterEventException::class)
    suspend fun registerNewSale(userUid: String)

    @Throws(RegisterEventException::class)
    suspend fun registerNewCreation(userUid: String)
}