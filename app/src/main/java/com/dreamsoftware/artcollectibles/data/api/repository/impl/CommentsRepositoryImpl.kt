package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.CommentMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.SaveCommentMapper
import com.dreamsoftware.artcollectibles.data.api.repository.ICommentsRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ICommentsDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.CreateComment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class CommentsRepositoryImpl(
    private val commentsDataSource: ICommentsDataSource,
    private val commentMapper: CommentMapper,
    private val userRepository: IUserRepository,
    private val saveCommentMapper: SaveCommentMapper,
    private val artCollectibleMemoryCacheDataSource: IArtCollectibleMemoryCacheDataSource
): ICommentsRepository {

    @Throws(SaveCommentDataException::class)
    override suspend fun save(comment: CreateComment): Comment = withContext(Dispatchers.IO) {
        try {
            saveCommentMapper.mapInToOut(comment).let {
                with(commentsDataSource) {
                    save(it)
                    commentMapper.mapInToOut(getCommentById(it.uid).let {
                        CommentMapper.InputData(
                            commentDTO = it,
                            userInfoDTO = userRepository.get(it.userUid, false)
                        )
                    }).also {
                        with(artCollectibleMemoryCacheDataSource) {
                            if(hasKey(it.tokenId)) {
                                findByKey(it.tokenId).let {
                                    it.copy(commentsCount = it.commentsCount + 1)
                                }.let {
                                    save(it.id, it)
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            throw SaveCommentDataException("An error occurred when trying to save a comment")
        }
    }

    @Throws(DeleteCommentDataException::class)
    override suspend fun delete(tokenId: BigInteger, uid: String) {
        withContext(Dispatchers.IO) {
            try {
                commentsDataSource.delete(tokenId, uid).also {
                    with(artCollectibleMemoryCacheDataSource) {
                        if(hasKey(tokenId)) {
                            findByKey(tokenId).let {
                                it.copy(commentsCount = it.commentsCount - 1)
                            }.let {
                                save(it.id, it)
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                throw DeleteCommentDataException("An error occurred when trying to delete comment", ex)
            }
        }
    }

    @Throws(CountCommentsByTokenDataException::class)
    override suspend fun count(tokenId: BigInteger): Long = withContext(Dispatchers.IO) {
        try {
            commentsDataSource.count(tokenId)
        } catch (ex: Exception) {
            throw CountCommentsByTokenDataException("An error occurred when trying to count comments")
        }
    }

    @Throws(GetCommentByIdDataException::class)
    override suspend fun getCommentByUid(uid: String): Comment = withContext(Dispatchers.IO) {
        try {
            commentMapper.mapInToOut(commentsDataSource.getCommentById(uid).let {
                CommentMapper.InputData(
                    commentDTO = it,
                    userInfoDTO = userRepository.get(it.userUid, true)
                )
            })
        } catch (ex: Exception) {
            throw GetCommentByIdDataException("An error occurred when trying to get comment", ex)
        }
    }

    @Throws(GetCommentsByTokenDataException::class)
    override suspend fun getByTokenId(tokenId: BigInteger): Iterable<Comment> = withContext(Dispatchers.IO) {
        try {
            commentMapper.mapInListToOutList(commentsDataSource.getByTokenId(tokenId).map {
                async {
                    CommentMapper.InputData(
                        commentDTO = it,
                        userInfoDTO = userRepository.get(it.userUid, false)
                    )
                }
            }.awaitAll())
        } catch (ex: Exception) {
            throw GetCommentsByTokenDataException("An error occurred when trying to get comments by token id", ex)
        }
    }

    @Throws(GetCommentsByTokenDataException::class)
    override suspend fun getLastCommentsByToken(tokenId: BigInteger, limit: Int): Iterable<Comment> = withContext(Dispatchers.IO) {
        try {
            commentMapper.mapInListToOutList(commentsDataSource.getLastCommentsByToken(tokenId, limit).map {
                async {
                    CommentMapper.InputData(
                        commentDTO = it,
                        userInfoDTO = userRepository.get(it.userUid, false)
                    )
                }
            }.awaitAll())
        } catch (ex: Exception) {
            throw GetCommentsByTokenDataException("An error occurred when trying to get comments by token id", ex)
        }
    }
}