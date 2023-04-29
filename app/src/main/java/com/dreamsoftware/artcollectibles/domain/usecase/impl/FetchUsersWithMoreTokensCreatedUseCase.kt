package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IStatisticsRepository
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchUsersWithMoreTokensCreatedUseCase(
    private val statisticsRepository: IStatisticsRepository
): BaseUseCaseWithParams<FetchUsersWithMoreTokensCreatedUseCase.Params, Iterable<UserMarketStatistic>>() {

    override suspend fun onExecuted(params: Params): Iterable<UserMarketStatistic> =
        statisticsRepository.fetchUsersWithMorePurchases(params.limit)

    data class Params(
        val limit: Int
    )
}