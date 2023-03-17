package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.CreateComment

interface ICommentsRepository {

    @Throws(SaveCommentDataException::class)
    suspend fun save(comment: CreateComment): Comment

    @Throws(DeleteCommentDataException::class)
    suspend fun delete(tokenId: String, uid: String)

    @Throws(GetCommentByIdDataException::class)
    suspend fun getCommentByUid(uid: String): Comment

    @Throws(GetCommentsByTokenDataException::class)
    suspend fun getByTokenId(tokenId: String): Iterable<Comment>
}