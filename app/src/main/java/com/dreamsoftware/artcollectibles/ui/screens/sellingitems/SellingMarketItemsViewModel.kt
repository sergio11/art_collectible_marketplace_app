package com.dreamsoftware.artcollectibles.ui.screens.sellingitems

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchSellingMarketItemsUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellingMarketItemsViewModel @Inject constructor(
    private val fetchSellingMarketItemsUseCase: FetchSellingMarketItemsUseCase
): SupportViewModel<SellingMarketItemsUiState>() {

    override fun onGetDefaultState(): SellingMarketItemsUiState = SellingMarketItemsUiState()

    fun load() {
        onLoading()
        fetchSellingMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = FetchSellingMarketItemsUseCase.Params(),
            onSuccess = ::onFetchSellingMarketItemsCompleted,
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

    private fun onFetchSellingMarketItemsCompleted(items: Iterable<ArtCollectibleForSale>) {
        updateState {
            it.copy(
                isLoading = false,
                sellingItems = items
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

data class SellingMarketItemsUiState(
    val isLoading: Boolean = false,
    val sellingItems: Iterable<ArtCollectibleForSale> = emptyList(),
    var error: Exception? = null
)