package com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
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
    private val getSimilarAuthorMarketItemsUseCase: GetSimilarAuthorMarketItemsUseCase,
    private val fetchMarketHistoryItemUseCase: FetchMarketHistoryItemUseCase
) : SupportViewModel<MarketUiState>() {

    companion object {
        const val SIMILAR_TOKENS_LIMIT = 6
    }

    override fun onGetDefaultState(): MarketUiState = MarketUiState()

    fun loadMarketHistoryItemDetail(marketItemDetail: BigInteger) {
        loadDetail(
            id = marketItemDetail,
            isMarketHistoryItem = true
        )
    }

    fun loadForSaleMarketItemDetail(tokenId: BigInteger) {
        loadDetail(
            id = tokenId,
            isMarketHistoryItem = false
        )
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
    private fun loadDetail(id: BigInteger, isMarketHistoryItem: Boolean) {
        onLoading()
        viewModelScope.launch {
            runCatching {
                val fetchMarketItemDeferred = async {
                    if(isMarketHistoryItem) {
                        fetchMarketHistoryItem(id)
                    } else {
                        fetchItemForSale(id)
                    }
                }
                val loadAuthUserDetailDeferred = async { loadAuthUserDetail() }
                val marketItem = fetchMarketItemDeferred.await()
                val authUser = loadAuthUserDetailDeferred.await()
                updateState {
                    it.copy(
                        isLoading = false,
                        isMarketHistoryItem = isMarketHistoryItem,
                        artCollectibleForSale = marketItem,
                        isTokenSeller = marketItem.seller.uid == authUser.uid,
                        isTokenAuthor = marketItem.token.author.uid == authUser.uid
                    )
                }
                if(!isMarketHistoryItem) {
                    fetchCurrentBalance()
                }
                fetchSimilarMarketItemsUseCase(marketItem.token.id)
                fetchSimilarAuthorMarketItemsUseCase(marketItem.token.id)
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

    private fun fetchSimilarMarketItemsUseCase(tokenId: BigInteger) {
        getSimilarMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = GetSimilarMarketItemsUseCase.Params(
                tokenId = tokenId,
                count = SIMILAR_TOKENS_LIMIT
            ),
            onSuccess = { items ->
                updateState {
                    it.copy(similarMarketItems = items)
                }
            },
            onError = {
                it.printStackTrace()
                // ignore error
            }
        )
    }

    private fun fetchSimilarAuthorMarketItemsUseCase(tokenId: BigInteger) {
        getSimilarAuthorMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = GetSimilarAuthorMarketItemsUseCase.Params(
                tokenId = tokenId,
                count = SIMILAR_TOKENS_LIMIT
            ),
            onSuccess = { items ->
                updateState {
                    it.copy(similarAuthorMarketItems = items)
                }
            },
            onError = {
                it.printStackTrace()
                // ignore error
            }
        )
    }

    private suspend fun fetchItemForSale(tokenId: BigInteger) = fetchItemForSaleUseCase.invoke(
        scope = viewModelScope,
        params = FetchItemForSaleUseCase.Params(tokenId)
    )

    private suspend fun fetchMarketHistoryItem(marketItemId: BigInteger) = fetchMarketHistoryItemUseCase.invoke(
        scope = viewModelScope,
        params = FetchMarketHistoryItemUseCase.Params(marketItemId)
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
            it.printStackTrace()
            // ignore error
        }
    )
}

data class MarketUiState(
    val isLoading: Boolean = false,
    val isTokenSeller: Boolean = false,
    val itemBought: Boolean = false,
    val isMarketHistoryItem: Boolean = false,
    val itemWithdrawnFromSale: Boolean = false,
    val isConfirmBuyItemDialogVisible: Boolean = false,
    val isTokenAuthor: Boolean = false,
    val artCollectibleForSale: ArtCollectibleForSale? = null,
    val similarMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val similarAuthorMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val enoughFunds: Boolean = false,
    val isConfirmWithDrawFromSaleDialogVisible: Boolean = false,
    val error: Throwable? = null
)