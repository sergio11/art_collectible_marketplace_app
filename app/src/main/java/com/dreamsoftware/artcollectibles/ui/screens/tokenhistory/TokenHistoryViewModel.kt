package com.dreamsoftware.artcollectibles.ui.screens.tokenhistory

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetTokenMarketHistoryUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import javax.inject.Inject

/**
 * Token History View Model
 */
@HiltViewModel
class TokenHistoryViewModel @Inject constructor(
    private val getTokenMarketHistoryUseCase: GetTokenMarketHistoryUseCase
) : SupportViewModel<TokenHistoryUiState>() {

    override fun onGetDefaultState(): TokenHistoryUiState = TokenHistoryUiState()

    fun load(tokenId: BigInteger) {
        onLoading()
        getTokenMarketHistoryUseCase.invoke(
            scope = viewModelScope,
            params = GetTokenMarketHistoryUseCase.Params(tokenId),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(tokenHistoryItems: Iterable<ArtCollectibleForSale>) {
        updateState {
            it.copy(
                isLoading = false,
                tokenHistory = tokenHistoryItems
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

data class TokenHistoryUiState(
    val isLoading: Boolean = false,
    val tokenHistory: Iterable<ArtCollectibleForSale> = emptyList(),
    var error: Exception? = null
)