package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICommentsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.CommentMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.SaveCommentMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.CommentDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveCommentDTO
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

internal class CommentsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore,
    private val commentMapper: CommentMapper,
    private val saveCommentMapper: SaveCommentMapper
) : ICommentsDataSource {

    private companion object {
        const val COLLECTION_NAME = "comments"
        const val COMMENTS_FIELD_NAME = "comments"
        const val COMMENTS_COUNT_SUFFIX = "_count"
        const val COUNT_FIELD_NAME = "count"
    }

    @Throws(SaveCommentException::class)
    override suspend fun save(comment: SaveCommentDTO): Unit = withContext(Dispatchers.IO) {
        try {
            val key = comment.tokenId.toString()
            firebaseStore.collection(COLLECTION_NAME).apply {
                document(key)
                    .set(hashMapOf(COMMENTS_FIELD_NAME to FieldValue.arrayUnion(
                        saveCommentMapper.mapInToOut(comment))), SetOptions.merge())
                    .await()
                document(key + COMMENTS_COUNT_SUFFIX).set(
                    hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)),
                    SetOptions.merge()
                ).await()
            }
        } catch (ex: Exception) {
            throw SaveCommentException(
                "An error occurred when trying to save comment information",
                ex
            )
        }
    }

    @Throws(CountCommentsException::class)
    override suspend fun count(tokenId: String): Long = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(tokenId + COMMENTS_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: Exception) {
            throw CountCommentsException(
                "An error occurred when trying to count comments",
                ex
            )
        }
    }

    @Throws(GetCommentsByTokenIdException::class)
    override suspend fun getByTokenId(tokenId: String): Iterable<CommentDTO> =
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME)
                    .document(tokenId)
                    .get()
                    .await()?.data?.let { it[COMMENTS_FIELD_NAME] as? Iterable<Map<String, Any?>> }
                    ?.let { commentMapper.mapInListToOutList(it) }
                    ?: throw GetCommentsByTokenIdException("No comments found")
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw GetCommentsByTokenIdException(
                    "An error occurred when trying to get followers",
                    ex
                )
            }
        }
}