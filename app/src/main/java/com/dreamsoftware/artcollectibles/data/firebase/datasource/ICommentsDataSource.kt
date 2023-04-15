package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.model.CommentDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveCommentDTO
import java.math.BigInteger

interface ICommentsDataSource {

    /**
     * Save comment
     * @param comment
     */
    @Throws(SaveCommentException::class)
    suspend fun save(comment: SaveCommentDTO)

    /**
     * Delete comment
     * @param tokenId
     * @param uid
     */
    @Throws(DeleteCommentException::class)
    suspend fun delete(tokenId: BigInteger, uid: String)

    /**
     * Count comments
     * @param tokenId
     */
    @Throws(CountCommentsException::class)
    suspend fun count(tokenId: BigInteger): Long

    /**
     * Get Comment By id Exception
     */
    @Throws(GetCommentByIdException::class)
    suspend fun getCommentById(uid: String): CommentDTO

    /**
     * Get by token id
     * @param tokenId
     */
    @Throws(GetCommentsByTokenIdException::class)
    suspend fun getByTokenId(tokenId: BigInteger): Iterable<CommentDTO>

    /**
     * Get last comments by token
     * @param tokenId
     * @param limit
     */
    @Throws(GetCommentsByTokenIdException::class)
    suspend fun getLastCommentsByToken(tokenId: BigInteger, limit: Int): Iterable<CommentDTO>
}