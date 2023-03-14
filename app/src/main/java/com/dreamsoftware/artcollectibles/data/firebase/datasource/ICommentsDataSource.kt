package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.CountCommentsException
import com.dreamsoftware.artcollectibles.data.firebase.exception.GetCommentsByTokenIdException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveCommentException
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
     * Count comments
     * @param tokenId
     */
    @Throws(CountCommentsException::class)
    suspend fun count(tokenId: String): Long

    /**
     * Get by token id
     * @param tokenId
     */
    @Throws(GetCommentsByTokenIdException::class)
    suspend fun getByTokenId(tokenId: String): Iterable<CommentDTO>
}