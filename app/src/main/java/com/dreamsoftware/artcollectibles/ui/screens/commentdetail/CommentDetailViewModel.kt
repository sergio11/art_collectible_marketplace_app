package com.dreamsoftware.artcollectibles.ui.screens.commentdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.usecase.impl.DeleteCommentUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCommentDetailUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentDetailViewModel @Inject constructor(
    private val getCommentDetailUseCase: GetCommentDetailUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) : SupportViewModel<CommentDetailUiState>() {

    override fun onGetDefaultState(): CommentDetailUiState = CommentDetailUiState()

    fun loadDetail(uid: String) {
        onLoading()
        getCommentDetailUseCase.invoke(
            scope = viewModelScope,
            params = GetCommentDetailUseCase.Params(uid),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    fun deleteComment(comment: Comment) {
        onLoading()
        with(comment) {
            deleteCommentUseCase.invoke(
                scope = viewModelScope,
                params = DeleteCommentUseCase.Params(
                    tokenId = tokenId,
                    commentUid = uid
                ),
                onSuccess = { onCommentDeleted() },
                onError = ::onErrorOccurred
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(isLoading = false)
        }
    }

    private fun onSuccess(comment: Comment) {
        updateState {
            it.copy(
                isLoading = false,
                comment = comment
            )
        }
    }

    private fun onCommentDeleted() {
        updateState {
            it.copy(
                isLoading = false,
                commentDeleted = true,
                comment = null
            )
        }
    }
}

data class CommentDetailUiState(
    val isLoading: Boolean = false,
    val commentDeleted: Boolean = false,
    val comment: Comment? = null
)