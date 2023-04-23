package com.dreamsoftware.artcollectibles.ui.screens.availableitems

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchAvailableMarketItemsUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AvailableMarketItemsViewModel @Inject constructor(
    private val fetchAvailableMarketItemsUseCase: FetchAvailableMarketItemsUseCase
): SupportViewModel<AvailableMarketItemsUiState>() {

    override fun onGetDefaultState(): AvailableMarketItemsUiState = AvailableMarketItemsUiState()

    fun load() {
        onLoading()
        fetchAvailableMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = FetchAvailableMarketItemsUseCase.Params(),
            onSuccess = ::onFetchAvailableMarketItemsCompleted,
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

    private fun onFetchAvailableMarketItemsCompleted(items: Iterable<ArtCollectibleForSale>) {
        updateState {
            it.copy(
                isLoading = false,
                availableItems = items
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

data class AvailableMarketItemsUiState(
    val isLoading: Boolean = false,
    val availableItems: Iterable<ArtCollectibleForSale> = emptyList(),
    var error: Exception? = null
)