package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchItemForSaleUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class MarketItemDetailViewModel @Inject constructor(
    private val fetchItemForSaleUseCase: FetchItemForSaleUseCase
) : SupportViewModel<MarketUiState>() {

    override fun onGetDefaultState(): MarketUiState = MarketUiState()

    fun loadDetail(tokenId: BigInteger) {
        onLoading()
        fetchItemForSaleUseCase.invoke(
            scope = viewModelScope,
            params = FetchItemForSaleUseCase.Params(tokenId),
            onSuccess = ::onFetchItemForSaleSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFetchItemForSaleSuccess(artCollectibleForSale: ArtCollectibleForSale) {
        updateState {
            it.copy(
                isLoading = false,
                artCollectibleForSale = artCollectibleForSale
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(isLoading = false)
        }
    }
}

data class MarketUiState(
    val isLoading: Boolean = false,
    val artCollectibleForSale: ArtCollectibleForSale? = null
)