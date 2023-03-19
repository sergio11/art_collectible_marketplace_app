package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IVisitorsRepository
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetVisitorsByTokenUseCase(
    private val visitorsRepository: IVisitorsRepository
): BaseUseCaseWithParams<GetVisitorsByTokenUseCase.Params, Iterable<UserInfo>>() {

    override suspend fun onExecuted(params: Params): Iterable<UserInfo> =
        visitorsRepository.getByTokenId(params.tokenId)

    data class Params(
        val tokenId: String
    )
}