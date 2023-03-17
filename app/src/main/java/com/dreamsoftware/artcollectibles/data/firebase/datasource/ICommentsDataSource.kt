package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.model.CommentDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveCommentDTO

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
    suspend fun delete(tokenId: String, uid: String)

    /**
     * Count comments
     * @param tokenId
     */
    @Throws(CountCommentsException::class)
    suspend fun count(tokenId: String): Long

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
    suspend fun getByTokenId(tokenId: String): Iterable<CommentDTO>
}