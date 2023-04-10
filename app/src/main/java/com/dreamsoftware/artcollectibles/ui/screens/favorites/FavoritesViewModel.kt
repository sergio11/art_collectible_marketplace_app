package com.dreamsoftware.artcollectibles.ui.screens.favorites

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetUserLikesByTokenUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getUserLikesByTokenUseCase: GetUserLikesByTokenUseCase
) : SupportViewModel<FavoritesUiState>() {

    override fun onGetDefaultState(): FavoritesUiState = FavoritesUiState()

    fun load(tokenId: BigInteger) {
        onLoading()
        getUserLikesByTokenUseCase.invoke(
            scope = viewModelScope,
            params = GetUserLikesByTokenUseCase.Params(tokenId),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(userResult: Iterable<UserInfo>) {
        updateState {
            it.copy(
                isLoading = false,
                userResult = userResult
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

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val userResult: Iterable<UserInfo> = emptyList(),
    var error: Exception? = null
)