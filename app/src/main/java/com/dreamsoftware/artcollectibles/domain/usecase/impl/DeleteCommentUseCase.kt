package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ICommentsRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class DeleteCommentUseCase(
    private val commentsRepository: ICommentsRepository
): BaseUseCaseWithParams<DeleteCommentUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) = with(params) {
        commentsRepository.delete(tokenId, commentUid)
    }

    data class Params(
        val tokenId: BigInteger,
        val commentUid: String
    )
}