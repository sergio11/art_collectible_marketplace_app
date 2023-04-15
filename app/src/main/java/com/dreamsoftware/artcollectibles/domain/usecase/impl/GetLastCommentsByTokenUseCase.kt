package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ICommentsRepository
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class GetLastCommentsByTokenUseCase(
    private val commentsRepository: ICommentsRepository
): BaseUseCaseWithParams<GetLastCommentsByTokenUseCase.Params, Iterable<Comment>>() {

    override suspend fun onExecuted(params: Params): Iterable<Comment> = with(params) {
        commentsRepository.getLastCommentsByToken(tokenId, limit)
    }

    data class Params(
        val tokenId: BigInteger,
        val limit: Int
    )
}