package com.dreamsoftware.artcollectibles.ui.screens.tokens

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetTokensCreatedByUserUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetTokensOwnedByUserUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Tokens View Model
 * @param getTokensOwnedByUserUseCase
 * @param getTokensCreatedByUserUseCase
 */
@HiltViewModel
class TokensViewModel @Inject constructor(
    private val getTokensOwnedByUserUseCase: GetTokensOwnedByUserUseCase,
    private val getTokensCreatedByUserUseCase: GetTokensCreatedByUserUseCase
) : SupportViewModel<TokensUiState>() {

    override fun onGetDefaultState(): TokensUiState = TokensUiState()

    fun loadTokensOwnedBy(userAddress: String) {
        onLoading()
        getTokensOwnedByUserUseCase.invoke(
            scope = viewModelScope,
            params = GetTokensOwnedByUserUseCase.Params(userAddress),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    fun loadTokensCreatedBy(userAddress: String) {
        onLoading()
        getTokensCreatedByUserUseCase.invoke(
            scope = viewModelScope,
            params = GetTokensCreatedByUserUseCase.Params(userAddress),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(tokensResult: Iterable<ArtCollectible>) {
        updateState {
            it.copy(
                isLoading = false,
                tokensResult = tokensResult
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

data class TokensUiState(
    val isLoading: Boolean = false,
    val tokensResult: Iterable<ArtCollectible> = emptyList(),
    var error: Exception? = null
)