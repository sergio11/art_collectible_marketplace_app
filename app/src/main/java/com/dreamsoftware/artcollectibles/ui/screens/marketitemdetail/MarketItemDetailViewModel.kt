package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.google.common.collect.Iterables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class MarketItemDetailViewModel @Inject constructor(
    private val fetchItemForSaleUseCase: FetchItemForSaleUseCase,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val buyItemUseCase: BuyItemUseCase,
    private val withdrawFromSaleUseCase: WithdrawFromSaleUseCase,
    private val getSimilarMarketItemsUseCase: GetSimilarMarketItemsUseCase,
    private val getCurrentBalanceUseCase: GetCurrentBalanceUseCase,
) : SupportViewModel<MarketUiState>() {

    companion object {
        const val SIMILAR_TOKENS_LIMIT = 6
    }

    override fun onGetDefaultState(): MarketUiState = MarketUiState()

    fun loadDetail(tokenId: BigInteger) {
        onLoading()
        loadAllDataForToken(tokenId)
    }

    fun buyItem(tokenId: BigInteger) {
        onLoading()
        buyItemUseCase.invoke(
            scope = viewModelScope,
            params = BuyItemUseCase.Params(tokenId),
            onSuccess = { onBuyItemSuccess() },
            onError = ::onErrorOccurred
        )
    }

    fun onConfirmBuyItemDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isConfirmBuyItemDialogVisible = isVisible) }
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

    fun onConfirmWithDrawFromSaleDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isConfirmWithDrawFromSaleDialogVisible = isVisible) }
    }

    /**
     * Private Methods
     */
    private fun loadAllDataForToken(tokenId: BigInteger) {
        viewModelScope.launch {
            runCatching {
                val fetchItemForSaleDeferred = async { fetchItemForSale(tokenId) }
                val loadAuthUserDetailDeferred = async { loadAuthUserDetail() }
                val marketItem = fetchItemForSaleDeferred.await()
                val authUser = loadAuthUserDetailDeferred.await()
                updateState {
                    it.copy(
                        isLoading = false,
                        artCollectibleForSale = marketItem,
                        isTokenSeller = marketItem.seller.uid == authUser.uid,
                        isTokenAuthor = marketItem.token.author.uid == authUser.uid
                    )
                }
                fetchCurrentBalance()
                getSimilarMarketItemsUseCase(tokenId)
            }.onFailure(::onErrorOccurred)
        }
    }

    private fun onLoading() {
        updateState { it.copy(
            isLoading = true,
            isConfirmBuyItemDialogVisible = false
        ) }
    }

    private fun onErrorOccurred(ex: Throwable) {
        ex.printStackTrace()
        Log.d("ART_COLL", "onErrorOccurred ${ex.message} CALLED!")
        updateState {
            it.copy(
                isLoading = false,
                isConfirmBuyItemDialogVisible = false,
                error = ex
            )
        }
    }

    private fun onBuyItemSuccess() {
        updateState {
            it.copy(
                isLoading = false,
                itemBought = true,
                isConfirmBuyItemDialogVisible = false
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

    private fun fetchCurrentBalance() = getCurrentBalanceUseCase.invoke(
        scope = viewModelScope,
        params = GetCurrentBalanceUseCase.Params(),
        onSuccess = { currentBalance ->
            updateState {
                it.copy(enoughFunds = it.artCollectibleForSale?.price?.priceInWei?.let { marketItemPrice ->
                    currentBalance.balanceInWei >= marketItemPrice
                } ?: false)
            }
        },
        onError = {
            // ignore error
        }
    )
}

data class MarketUiState(
    val isLoading: Boolean = false,
    val isTokenSeller: Boolean = false,
    val itemBought: Boolean = false,
    val itemWithdrawnFromSale: Boolean = false,
    val isConfirmBuyItemDialogVisible: Boolean = false,
    val isTokenAuthor: Boolean = false,
    val artCollectibleForSale: ArtCollectibleForSale? = null,
    val similarMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val enoughFunds: Boolean = false,
    val isConfirmWithDrawFromSaleDialogVisible: Boolean = false,
    val error: Throwable? = null
)