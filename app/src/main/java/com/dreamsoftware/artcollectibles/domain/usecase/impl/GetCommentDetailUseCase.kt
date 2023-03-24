package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ICommentsRepository
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetCommentDetailUseCase(
    private val commentsRepository: ICommentsRepository
): BaseUseCaseWithParams<GetCommentDetailUseCase.Params, Comment>() {

    override suspend fun onExecuted(params: Params): Comment =
        commentsRepository.getCommentByUid(params.uid)

    data class Params(
        val uid: String
    )
}