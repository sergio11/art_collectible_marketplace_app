package com.dreamsoftware.artcollectibles.ui.screens.markethistory

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchMarketHistoryUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketHistoryViewModel @Inject constructor(
    private val fetchMarketHistoryUseCase: FetchMarketHistoryUseCase
): SupportViewModel<MarketHistoryUiState>() {

    override fun onGetDefaultState(): MarketHistoryUiState = MarketHistoryUiState()

    fun load() {
        onLoading()
        fetchMarketHistoryUseCase.invoke(
            scope = viewModelScope,
            params = FetchMarketHistoryUseCase.Params(),
            onSuccess = ::onFetchMarketHistoryCompleted,
            onError = ::onErrorOccurred
        )
    }

    private fun onLoading() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun onFetchMarketHistoryCompleted(items: Iterable<ArtCollectibleForSale>) {
        updateState {
            it.copy(
                isLoading = false,
                marketItems = items
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(
                isLoading = false,
                error = ex
            )
        }
    }
}

data class MarketHistoryUiState(
    val isLoading: Boolean = false,
    val marketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    var error: Exception? = null
)