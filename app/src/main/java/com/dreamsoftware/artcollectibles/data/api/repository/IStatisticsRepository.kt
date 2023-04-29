package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.FetchMarketStatisticsDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterEventDataException
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic

interface IStatisticsRepository {

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchUsersWithMorePurchases(limit: Int): Iterable<UserMarketStatistic>

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchUsersWithMoreSales(limit: Int): Iterable<UserMarketStatistic>

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchUsersWithMoreTokensCreated(limit: Int): Iterable<UserMarketStatistic>

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewPurchase(userUid: String)

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewSale(userUid: String)

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewCreation(userUid: String)
}