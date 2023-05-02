package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IStatisticsRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchMostCancelledTokensUseCase(
    private val statisticsRepository: IStatisticsRepository
): BaseUseCaseWithParams<FetchMostCancelledTokensUseCase.Params, Iterable<ArtCollectibleMarketStatistic>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleMarketStatistic> =
        statisticsRepository.fetchMostCancelledTokens(params.limit)

    data class Params(
        val limit: Int
    )
}