package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.FetchMarketStatisticsDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterEventDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketStatistic
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import java.math.BigInteger

interface IStatisticsRepository {

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchUsersWithMorePurchases(limit: Int): Iterable<UserMarketStatistic>

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchUsersWithMoreSales(limit: Int): Iterable<UserMarketStatistic>

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchUsersWithMoreTokensCreated(limit: Int): Iterable<UserMarketStatistic>

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchMostSoldTokens(limit: Int): Iterable<ArtCollectibleMarketStatistic>

    @Throws(FetchMarketStatisticsDataException::class)
    suspend fun fetchMostCancelledTokens(limit: Int): Iterable<ArtCollectibleMarketStatistic>

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewPurchase(userUid: String)

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewSale(userUid: String)

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewCreation(userUid: String)

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewTokenSold(tokenId: BigInteger)

    @Throws(RegisterEventDataException::class)
    suspend fun registerNewTokenCancellation(tokenId: BigInteger)
}