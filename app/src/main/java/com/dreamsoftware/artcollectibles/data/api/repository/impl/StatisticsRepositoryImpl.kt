package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.FetchMarketStatisticsDataException
import com.dreamsoftware.artcollectibles.data.api.exception.RegisterEventDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.ArtCollectibleMarketStatisticMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserMarketStatisticMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IStatisticsRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStatisticsDataSource
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketStatistic
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class StatisticsRepositoryImpl(
    private val statisticsDataSource: IStatisticsDataSource,
    private val userRepository: IUserRepository,
    private val userMarketStatisticMapper: UserMarketStatisticMapper,
    private val artCollectibleMarketStatisticMapper: ArtCollectibleMarketStatisticMapper,
    private val artCollectibleRepository: IArtCollectibleRepository
): IStatisticsRepository {

    @Throws(FetchMarketStatisticsDataException::class)
    override suspend fun fetchUsersWithMorePurchases(limit: Int): Iterable<UserMarketStatistic> = withContext(Dispatchers.IO) {
        try {
            statisticsDataSource.fetchUsersWithMorePurchases(limit)
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map {
                    userMarketStatisticMapper.mapInToOut(UserMarketStatisticMapper.InputData(
                        userInfo = userRepository.get(uid = it.key, fullDetail = false),
                        marketStatisticDTO = it
                    ))
                }.toList()
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FetchMarketStatisticsDataException(
                "An error occurred when trying to fetch users with more purchases",
                ex
            )
        }
    }

    @Throws(FetchMarketStatisticsDataException::class)
    override suspend fun fetchUsersWithMoreSales(limit: Int): Iterable<UserMarketStatistic> = withContext(Dispatchers.IO) {
        try {
            statisticsDataSource.fetchUsersWithMoreSales(limit)
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map {
                    userMarketStatisticMapper.mapInToOut(UserMarketStatisticMapper.InputData(
                        userInfo = userRepository.get(uid = it.key, fullDetail = false),
                        marketStatisticDTO = it
                    ))
                }.toList()
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FetchMarketStatisticsDataException(
                "An error occurred when trying to fetch users with more sales",
                ex
            )
        }
    }

    @Throws(FetchMarketStatisticsDataException::class)
    override suspend fun fetchUsersWithMoreTokensCreated(limit: Int): Iterable<UserMarketStatistic> = withContext(Dispatchers.IO) {
        try {
            statisticsDataSource.fetchUsersWithMoreTokensCreated(limit)
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map {
                    userMarketStatisticMapper.mapInToOut(UserMarketStatisticMapper.InputData(
                        userInfo = userRepository.get(uid = it.key, fullDetail = false),
                        marketStatisticDTO = it
                    ))
                }.toList()
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FetchMarketStatisticsDataException(
                "An error occurred when trying to fetch users with more tokens created",
                ex
            )
        }
    }

    @Throws(FetchMarketStatisticsDataException::class)
    override suspend fun fetchMostSoldTokens(limit: Int): Iterable<ArtCollectibleMarketStatistic> = withContext(Dispatchers.IO) {
        try {
            statisticsDataSource.fetchMostSoldTokens(limit)
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map {
                    artCollectibleMarketStatisticMapper.mapInToOut(ArtCollectibleMarketStatisticMapper.InputData(
                        artCollectible = artCollectibleRepository.getTokenById(it.key.toBigInteger()),
                        marketStatisticDTO = it
                    ))
                }.toList()
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FetchMarketStatisticsDataException(
                "An error occurred when trying to fetch most sold tokens",
                ex
            )
        }
    }

    @Throws(FetchMarketStatisticsDataException::class)
    override suspend fun fetchMostCancelledTokens(limit: Int): Iterable<ArtCollectibleMarketStatistic> = withContext(Dispatchers.IO)  {
        try {
            statisticsDataSource.fetchMostCancelledTokens(limit)
                .asFlow()
                .flowOn(Dispatchers.IO)
                .map {
                    artCollectibleMarketStatisticMapper.mapInToOut(ArtCollectibleMarketStatisticMapper.InputData(
                        artCollectible = artCollectibleRepository.getTokenById(it.key.toBigInteger()),
                        marketStatisticDTO = it
                    ))
                }.toList()
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw FetchMarketStatisticsDataException(
                "An error occurred when trying to fetch most cancelled tokens",
                ex
            )
        }
    }

    @Throws(RegisterEventDataException::class)
    override suspend fun registerNewPurchase(userUid: String) {
        withContext(Dispatchers.IO) {
            try {
                statisticsDataSource.registerNewPurchase(userUid)
            } catch (ex: Exception) {
                throw RegisterEventDataException("An error occurred when trying to register a new purchase", ex)
            }
        }
    }

    @Throws(RegisterEventDataException::class)
    override suspend fun registerNewSale(userUid: String) {
        withContext(Dispatchers.IO) {
            try {
                statisticsDataSource.registerNewSale(userUid)
            } catch (ex: Exception) {
                throw RegisterEventDataException("An error occurred when trying to register a new sale", ex)
            }
        }
    }

    @Throws(RegisterEventDataException::class)
    override suspend fun registerNewCreation(userUid: String) {
        withContext(Dispatchers.IO) {
            try {
                statisticsDataSource.registerNewCreation(userUid)
            } catch (ex: Exception) {
                throw RegisterEventDataException("An error occurred when trying to register a new token creation", ex)
            }
        }
    }

    @Throws(RegisterEventDataException::class)
    override suspend fun registerNewTokenSold(tokenId: BigInteger) {
        withContext(Dispatchers.IO) {
            try {
                statisticsDataSource.registerNewTokenSold(tokenId)
            } catch (ex: Exception) {
                throw RegisterEventDataException("An error occurred when trying to register a new token sold", ex)
            }
        }
    }

    @Throws(RegisterEventDataException::class)
    override suspend fun registerNewTokenCancellation(tokenId: BigInteger) {
        withContext(Dispatchers.IO) {
            try {
                statisticsDataSource.registerNewTokenCancellation(tokenId)
            } catch (ex: Exception) {
                throw RegisterEventDataException("An error occurred when trying to register a new token cancellation", ex)
            }
        }
    }
}