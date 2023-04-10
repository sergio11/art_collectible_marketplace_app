package com.dreamsoftware.artcollectibles.ui.screens.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.google.common.collect.Iterables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
        loadBasicData()
    }

    private fun loadBasicData() = viewModelScope.launch {
        runCatching {
            val fetchArtCollectibleCategoriesDeferred = async { fetchArtCollectibleCategories() }
            val fetchMoreFollowedUsersDeferred = async { fetchMoreFollowedUsers() }
            val fetchMoreLikedTokensDeferred = async { fetchMoreLikedTokensUseCase() }
            val categories = fetchArtCollectibleCategoriesDeferred.await()
            val moreFollowedUsers = fetchMoreFollowedUsersDeferred.await()
            val moreLikedTokens = fetchMoreLikedTokensDeferred.await()
            updateState {
                it.copy(
                    isLoading = false,
                    categories = categories,
                    moreFollowedUsers = moreFollowedUsers,
                    moreLikedTokens = moreLikedTokens
                )
            }
            loadMarketData()
        }.onFailure(::onErrorOccurred)
    }

    private fun loadMarketData() = viewModelScope.launch  {
        val fetchMarketplaceStatisticsDeferred = async { fetchMarketplaceStatistics() }
        val fetchAvailableMarketItemsDeferred = async { fetchAvailableMarketItems() }
        val fetchSellingMarketItemsDeferred = async { fetchSellingMarketItems() }
        val fetchMarketHistoryDeferred = async { fetchMarketHistory() }
        val availableMarketItems = fetchAvailableMarketItemsDeferred.await()
        val sellingMarketItems = fetchSellingMarketItemsDeferred.await()
        val marketHistory = fetchMarketHistoryDeferred.await()
        val marketplaceStatistics = fetchMarketplaceStatisticsDeferred.await()
        updateState {
            it.copy(
                availableMarketItems = availableMarketItems,
                sellingMarketItems = sellingMarketItems,
                marketHistory = marketHistory,
                marketplaceStatistics = marketplaceStatistics
            )
        }
    }

    private suspend fun fetchMarketplaceStatistics() = runCatching {
        fetchMarketplaceStatisticsUseCase.invoke(scope = viewModelScope)
    }.getOrNull()

    private suspend fun fetchAvailableMarketItems() = runCatching {
        fetchAvailableMarketItemsUseCase.invoke(scope = viewModelScope)
    }.getOrDefault(emptyList())

    private suspend fun fetchSellingMarketItems() = runCatching {
        fetchSellingMarketItemsUseCase.invoke(scope = viewModelScope)
    }.getOrDefault(emptyList())

    private suspend fun fetchMarketHistory() = runCatching {
        fetchMarketHistoryUseCase.invoke(scope = viewModelScope)
    }.getOrDefault(emptyList())

    private suspend fun fetchArtCollectibleCategories() =
        getArtCollectibleCategoriesUseCase.invoke(scope = viewModelScope)

    private suspend fun fetchMoreFollowedUsers() =
        getMoreFollowedUsersUseCase.invoke(scope = viewModelScope)

    private suspend fun fetchMoreLikedTokensUseCase() =
        getMoreLikedTokensUseCase.invoke(scope = viewModelScope)

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onErrorOccurred(ex: Throwable) {
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