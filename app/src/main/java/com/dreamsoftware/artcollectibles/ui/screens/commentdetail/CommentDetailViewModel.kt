package com.dreamsoftware.artcollectibles.ui.screens.commentdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.DeleteCommentUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAuthUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCommentDetailUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetTokensCreatedByUserUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentDetailViewModel @Inject constructor(
    private val getCommentDetailUseCase: GetCommentDetailUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val getTokensCreatedByUserUseCase: GetTokensCreatedByUserUseCase,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase
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

    fun onConfirmDeleteCommentDialogVisibilityChanged(isVisible: Boolean) {
        updateState {
            it.copy(isConfirmDeleteCommentDialogVisible = isVisible)
        }
    }

    private fun loadTokensCreatedByUser(userAddress: String) {
        getTokensCreatedByUserUseCase.invoke(
            scope = viewModelScope,
            params = GetTokensCreatedByUserUseCase.Params(userAddress),
            onSuccess = ::onLoadTokensCreatedCompleted,
            onError = ::onErrorOccurred
        )
    }

    private fun loadAuthUserDetail() {
        getAuthUserProfileUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onLoadAuthUserDetailCompleted,
            onError = {
                it.printStackTrace()
                // ignore error
            }
        )
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
        loadTokensCreatedByUser(userAddress = comment.user.walletAddress)
        loadAuthUserDetail()
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

    private fun onLoadTokensCreatedCompleted(tokensCreated: Iterable<ArtCollectible>) {
        updateState { it.copy(tokensCreated = tokensCreated) }
    }

    private fun onLoadAuthUserDetailCompleted(authUser: UserInfo) {
        updateState {
            it.copy(isDeleteCommentEnabled = it.comment?.user?.uid == authUser.uid)
        }
    }
}

data class CommentDetailUiState(
    val isLoading: Boolean = false,
    val commentDeleted: Boolean = false,
    val comment: Comment? = null,
    val tokensCreated: Iterable<ArtCollectible> = emptyList(),
    val isDeleteCommentEnabled: Boolean = false,
    val isConfirmDeleteCommentDialogVisible: Boolean = false
)