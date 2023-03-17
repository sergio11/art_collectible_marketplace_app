package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.GetCommentsByTokenDataException
import com.dreamsoftware.artcollectibles.data.api.exception.SaveCommentDataException
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.CreateComment

interface ICommentsRepository {

    @Throws(SaveCommentDataException::class)
    suspend fun save(comment: CreateComment): Comment

    @Throws(GetCommentsByTokenDataException::class)
    suspend fun getByTokenId(tokenId: String): Iterable<Comment>
}