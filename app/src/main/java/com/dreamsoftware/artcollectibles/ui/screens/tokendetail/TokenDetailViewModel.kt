package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.impl.BurnTokenUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetTokenDetailUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class TokenDetailViewModel @Inject constructor(
    private val getTokenDetailUseCase: GetTokenDetailUseCase,
    private val burnTokenUseCase: BurnTokenUseCase
) : SupportViewModel<TokenDetailUiState>() {

    override fun onGetDefaultState(): TokenDetailUiState = TokenDetailUiState()

    fun loadDetail(tokenId: BigInteger) {
        viewModelScope.launch {
            onLoading()
            getTokenDetailUseCase.invoke(
                params = GetTokenDetailUseCase.Params(tokenId),
                onSuccess = ::onTokenDetailLoaded,
                onError = ::onErrorOccurred
            )
        }
    }

    fun burnToken(tokenId: BigInteger) {
        viewModelScope.launch {
            onLoading()
            burnTokenUseCase.invoke(
                params = BurnTokenUseCase.Params(tokenId),
                onSuccess = {
                    onTokenBurned()
                },
                onError = ::onErrorOccurred
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onTokenDetailLoaded(artCollectible: ArtCollectible) {
        updateState {
            it.copy(
                artCollectible = artCollectible,
                isLoading = false
            )
        }
    }

    private fun onTokenBurned() {
        updateState {
            it.copy(
                artCollectible = null,
                isLoading = false,
                isBurned = true
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState { it.copy(isLoading = false) }
    }
}

data class TokenDetailUiState(
    var artCollectible: ArtCollectible? = null,
    val isLoading: Boolean = false,
    val isBurned: Boolean = false
)