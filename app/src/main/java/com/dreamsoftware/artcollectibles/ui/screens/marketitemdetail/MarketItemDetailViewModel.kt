package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.BuyItemUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchItemForSaleUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAuthUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.WithdrawFromSaleUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class MarketItemDetailViewModel @Inject constructor(
    private val fetchItemForSaleUseCase: FetchItemForSaleUseCase,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val buyItemUseCase: BuyItemUseCase,
    private val withdrawFromSaleUseCase: WithdrawFromSaleUseCase
) : SupportViewModel<MarketUiState>() {

    override fun onGetDefaultState(): MarketUiState = MarketUiState()

    fun loadDetail(tokenId: BigInteger) {
        onLoading()
        loadAllDataForToken(tokenId)
    }

    fun buyItem(tokenId: BigInteger, price: BigInteger) {
        onLoading()
        buyItemUseCase.invoke(scope = viewModelScope,
            params = BuyItemUseCase.Params(tokenId, price),
            onSuccess = { onBuyItemSuccess() },
            onError = ::onErrorOccurred
        )
    }

    fun withDrawFromSale(tokenId: BigInteger) {
        withdrawFromSaleUseCase.invoke(
            scope = viewModelScope,
            params = WithdrawFromSaleUseCase.Params(tokenId),
            onBeforeStart = {
                updateState {
                    it.copy(
                        isLoading = true
                    )
                }
            },
            onSuccess = {
                onTokenWithdrawnFromSale()
            },
            onError = ::onErrorOccurred
        )
    }

    /**
     * Private Methods
     */
    private fun loadAllDataForToken(tokenId: BigInteger) {
        viewModelScope.launch {
            runCatching {
                val marketItem = fetchItemForSale(tokenId)
                val authUser = loadAuthUserDetail()
                updateState {
                    it.copy(
                        isLoading = false,
                        artCollectibleForSale = marketItem,
                        isTokenSeller = marketItem.seller.uid == authUser.uid,
                        isTokenAuthor = marketItem.token.author.uid == authUser.uid
                    )
                }
            }.onFailure(::onErrorOccurred)
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onErrorOccurred(ex: Throwable) {
        updateState {
            it.copy(isLoading = false)
        }
    }

    private fun onBuyItemSuccess() {
        updateState {
            it.copy(
                isLoading = false,
                itemBought = true
            )
        }
    }

    private fun onTokenWithdrawnFromSale() {
        updateState {
            it.copy(
                isLoading = false,
                itemWithdrawnFromSale = true
            )
        }
    }

    private suspend fun fetchItemForSale(tokenId: BigInteger) = fetchItemForSaleUseCase.invoke(
        scope = viewModelScope,
        params = FetchItemForSaleUseCase.Params(tokenId)
    )

    private suspend fun loadAuthUserDetail() = getAuthUserProfileUseCase.invoke(
        scope = viewModelScope
    )
}

data class MarketUiState(
    val isLoading: Boolean = false,
    val isTokenSeller: Boolean = false,
    val itemBought: Boolean = false,
    val itemWithdrawnFromSale: Boolean = false,
    val isTokenAuthor: Boolean = false,
    val artCollectibleForSale: ArtCollectibleForSale? = null
)