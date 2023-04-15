package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ICommentsRepository
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class GetCommentsByTokenUseCase(
    private val commentsRepository: ICommentsRepository
): BaseUseCaseWithParams<GetCommentsByTokenUseCase.Params, Iterable<Comment>>() {

    override suspend fun onExecuted(params: Params): Iterable<Comment> {
        return commentsRepository.getByTokenId(params.tokenId)
    }

    data class Params(
        val tokenId: BigInteger
    )
}