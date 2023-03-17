package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ICommentsRepository
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.CreateComment
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger
import java.util.*

class SaveCommentUseCase(
    private val commentsRepository: ICommentsRepository
): BaseUseCaseWithParams<SaveCommentUseCase.Params, Comment>() {

    override suspend fun onExecuted(params: Params): Comment = with(params) {
        commentsRepository.save(CreateComment(
            uid = UUID.randomUUID().toString(),
            comment = comment,
            userUid = userUid,
            tokenId = tokenId
        ))
    }

    data class Params(
        val comment: String,
        val userUid: String,
        val tokenId: BigInteger
    )
}