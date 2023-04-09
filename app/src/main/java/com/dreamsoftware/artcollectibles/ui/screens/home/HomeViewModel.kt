package com.dreamsoftware.artcollectibles.ui.screens.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.google.common.collect.Iterables
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchAvailableMarketItemsUseCase: FetchAvailableMarketItemsUseCase,
    private val fetchSellingMarketItemsUseCase: FetchSellingMarketItemsUseCase,
    private val fetchMarketplaceStatisticsUseCase: FetchMarketplaceStatisticsUseCase,
    private val fetchMarketHistoryUseCase: FetchMarketHistoryUseCase,
    private val getArtCollectibleCategoriesUseCase: GetArtCollectibleCategoriesUseCase,
    private val getMoreFollowedUsersUseCase: GetMoreFollowedUsersUseCase,
    private val getMoreLikedTokensUseCase: GetMoreLikedTokensUseCase
) : SupportViewModel<HomeUiState>() {

    companion object {
        const val AVAILABLE_MARKET_ITEMS_LIMIT = 6
        const val SELLING_MARKET_ITEMS_LIMIT = 6
        const val MARKET_HISTORY_ITEMS_LIMIT = 6
    }

    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    fun loadData() {
        onLoading()
        fetchMarketplaceStatistics()
        fetchAvailableMarketItems()
        fetchSellingMarketItems()
        fetchMarketHistory()
        fetchArtCollectibleCategories()
        getMoreFollowedUsers()
        getMoreLikedTokensUseCase()
    }

    private fun fetchMarketplaceStatistics() {
        fetchMarketplaceStatisticsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { marketplaceStatistics ->
                updateState {
                    it.copy(
                        marketplaceStatistics = marketplaceStatistics
                    )
                }
            },
            onError = {}
        )
    }

    private fun fetchAvailableMarketItems() {
        fetchAvailableMarketItemsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { availableMarketItems ->
                updateState {
                    it.copy(availableMarketItems = availableMarketItems)
                }
            },
            onError = {}
        )
    }

    private fun fetchSellingMarketItems() {
        fetchSellingMarketItemsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { sellingItems ->
                Log.d("ART_COLL", "fetchSellingMarketItems onSuccess -> ${Iterables.size(sellingItems)}")
                updateState {
                    it.copy(sellingMarketItems = sellingItems)
                }
            },
            onError = {
                it.printStackTrace()
                Log.d("ART_COLL", "fetchSellingMarketItems err -> ${it.message}")
            }
        )
    }

    private fun fetchMarketHistory() {
        fetchMarketHistoryUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { marketHistory ->
                updateState {
                    it.copy(marketHistory = marketHistory)
                }
            },
            onError = {})
    }

    private fun fetchArtCollectibleCategories() {
        getArtCollectibleCategoriesUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onCategoriesLoaded,
            onError = {
                it.printStackTrace()
                Log.d("ART_COLL", "it.message -> ${it.message}")
            }
        )
    }

    private fun getMoreFollowedUsers() {
        getMoreFollowedUsersUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onMoreFollowedUsers,
            onError = {
                it.printStackTrace()
                Log.d("ART_COLL", "it.message -> ${it.message}")
            }
        )
    }

    private fun getMoreLikedTokensUseCase() {
        getMoreLikedTokensUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onMoreLikedTokens,
            onError = {
                it.printStackTrace()
                Log.d("ART_COLL", "it.message -> ${it.message}")
            }
        )
    }

    private fun onCategoriesLoaded(categories: Iterable<ArtCollectibleCategory>) {
        updateState {
            it.copy(
                isLoading = false,
                categories = categories
            )
        }
    }

    private fun onMoreFollowedUsers(moreFollowedUsers: Iterable<UserInfo>) {
        updateState { it.copy(moreFollowedUsers = moreFollowedUsers) }
    }

    private fun onMoreLikedTokens(moreLikedTokens: Iterable<ArtCollectible>) {
        updateState { it.copy(moreLikedTokens = moreLikedTokens) }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
        Log.d("ART_COLL", "onErrorOccurred ${ex.message} CALLED!")
        updateState { it.copy(isLoading = false) }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val marketplaceStatistics: MarketplaceStatistics? = null,
    val categories: Iterable<ArtCollectibleCategory> = emptyList(),
    val availableMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val sellingMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val marketHistory: Iterable<ArtCollectibleForSale> = emptyList(),
    val moreFollowedUsers: Iterable<UserInfo> = emptyList(),
    val moreLikedTokens: Iterable<ArtCollectible> = emptyList()
)