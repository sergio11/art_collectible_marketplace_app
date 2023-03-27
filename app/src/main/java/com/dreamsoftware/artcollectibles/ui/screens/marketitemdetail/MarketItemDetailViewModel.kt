package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.google.common.collect.Iterables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class MarketItemDetailViewModel @Inject constructor(
    private val fetchItemForSaleUseCase: FetchItemForSaleUseCase,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val buyItemUseCase: BuyItemUseCase,
    private val withdrawFromSaleUseCase: WithdrawFromSaleUseCase,
    private val getSimilarMarketItemsUseCase: GetSimilarMarketItemsUseCase
) : SupportViewModel<MarketUiState>() {

    companion object {
        const val SIMILAR_TOKENS_LIMIT = 6
    }

    override fun onGetDefaultState(): MarketUiState = MarketUiState()

    fun loadDetail(tokenId: BigInteger) {
        onLoading()
        loadAllDataForToken(tokenId)
    }

    fun buyItem(tokenId: BigInteger, price: BigInteger) {
        onLoading()
        buyItemUseCase.invoke(
            scope = viewModelScope,
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
                getSimilarMarketItemsUseCase(tokenId)
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

    private fun getSimilarMarketItemsUseCase(tokenId: BigInteger) {
        getSimilarMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = GetSimilarMarketItemsUseCase.Params(
                tokenId = tokenId,
                count = SIMILAR_TOKENS_LIMIT
            ),
            onSuccess = { items ->
                Log.d("ART_COLL", "onSuccess - items ${Iterables.size(items)}")
                updateState {
                    it.copy(similarMarketItems = items)
                }
            },
            onError = {
                it.printStackTrace()
                Log.d("ART_COLL", "onError - ex ${it.message}")
                // ignore error
            }
        )
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
    val artCollectibleForSale: ArtCollectibleForSale? = null,
    val similarMarketItems: Iterable<ArtCollectibleForSale> = emptyList()
)