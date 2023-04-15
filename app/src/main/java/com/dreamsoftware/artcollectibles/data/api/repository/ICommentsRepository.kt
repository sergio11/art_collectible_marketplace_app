package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.CreateComment
import java.math.BigInteger

interface ICommentsRepository {

    @Throws(SaveCommentDataException::class)
    suspend fun save(comment: CreateComment): Comment

    @Throws(DeleteCommentDataException::class)
    suspend fun delete(tokenId: BigInteger, uid: String)

    @Throws(CountCommentsByTokenDataException::class)
    suspend fun count(tokenId: BigInteger): Long

    @Throws(GetCommentByIdDataException::class)
    suspend fun getCommentByUid(uid: String): Comment

    @Throws(GetCommentsByTokenDataException::class)
    suspend fun getByTokenId(tokenId: BigInteger): Iterable<Comment>

    @Throws(GetCommentsByTokenDataException::class)
    suspend fun getLastCommentsByToken(tokenId: BigInteger, limit: Int): Iterable<Comment>

}