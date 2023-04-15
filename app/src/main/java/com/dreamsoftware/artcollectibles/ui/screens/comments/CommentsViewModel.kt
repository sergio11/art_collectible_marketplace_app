package com.dreamsoftware.artcollectibles.ui.screens.comments

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCommentsByTokenUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import javax.inject.Inject

/**
 * Comments View Model
 */
@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getCommentsByTokenUseCase: GetCommentsByTokenUseCase
) : SupportViewModel<CommentsUiState>() {

    override fun onGetDefaultState(): CommentsUiState = CommentsUiState()

    fun load(tokenId: BigInteger) {
        onLoading()
        getCommentsByTokenUseCase.invoke(
            scope = viewModelScope,
            params = GetCommentsByTokenUseCase.Params(
                tokenId = tokenId
            ),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(comments: Iterable<Comment>) {
        updateState {
            it.copy(
                isLoading = false,
                comments = comments
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
        updateState {
            it.copy(
                isLoading = false,
                error = ex
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }
}

data class CommentsUiState(
    val isLoading: Boolean = false,
    val comments: Iterable<Comment> = emptyList(),
    var error: Exception? = null
)