package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IStatisticsRepository
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchUsersWithMoreSalesUseCase(
    private val statisticsRepository: IStatisticsRepository
): BaseUseCaseWithParams<FetchUsersWithMoreSalesUseCase.Params, Iterable<UserMarketStatistic>>() {

    override suspend fun onExecuted(params: Params): Iterable<UserMarketStatistic> =
        statisticsRepository.fetchUsersWithMoreSales(params.limit)

    data class Params(
        val limit: Int
    )
}