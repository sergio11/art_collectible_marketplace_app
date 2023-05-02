package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IStatisticsRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchMostSoldTokensUseCase(
    private val statisticsRepository: IStatisticsRepository
): BaseUseCaseWithParams<FetchMostSoldTokensUseCase.Params, Iterable<ArtCollectibleMarketStatistic>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectibleMarketStatistic> =
        statisticsRepository.fetchMostSoldTokens(params.limit)

    data class Params(
        val limit: Int
    )
}