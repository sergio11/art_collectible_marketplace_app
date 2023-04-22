package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IVisitorsRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetMostVisitedTokensUseCase(
    private val visitorsRepository: IVisitorsRepository
) : BaseUseCaseWithParams<GetMostVisitedTokensUseCase.Params, Iterable<ArtCollectible>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectible> = with(params) {
        visitorsRepository.getMostVisitedTokens(limit)
    }

    data class Params(
        val limit: Int
    )
}