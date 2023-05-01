package com.dreamsoftware.artcollectibles.ui.screens.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
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
    private val getMostFollowedUsersUseCase: GetMostFollowedUsersUseCase,
    private val getMostLikedTokensUseCase: GetMostLikedTokensUseCase,
    private val getMostVisitedTokensUseCase: GetMostVisitedTokensUseCase
) : SupportViewModel<HomeUiState>() {

    companion object {
        const val AVAILABLE_MARKET_ITEMS_LIMIT = 6
        const val SELLING_MARKET_ITEMS_LIMIT = 6
        const val MARKET_HISTORY_ITEMS_LIMIT = 6
        const val MOST_LIKED_TOKENS_LIMIT = 5
        const val MOST_FOLLOWED_USERS_LIMIT = 5
        const val MOST_VISITED_TOKENS_LIMIT = 5
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
            val fetchMoreVisitedTokensUseCaseDeferred = async { fetchMoreVisitedTokensUseCase() }
            val categories = fetchArtCollectibleCategoriesDeferred.await()
            val mostFollowedUsers = fetchMoreFollowedUsersDeferred.await()
            val mostLikedTokens = fetchMoreLikedTokensDeferred.await()
            val mostVisitedTokens = fetchMoreVisitedTokensUseCaseDeferred.await()
            updateState {
                it.copy(
                    isLoading = false,
                    categories = categories,
                    mostFollowedUsers = mostFollowedUsers,
                    mostLikedTokens = mostLikedTokens,
                    mostVisitedTokens = mostVisitedTokens
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
        fetchAvailableMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = FetchAvailableMarketItemsUseCase.Params(limit = AVAILABLE_MARKET_ITEMS_LIMIT)
        )
    }.getOrDefault(emptyList())

    private suspend fun fetchSellingMarketItems() = runCatching {
        fetchSellingMarketItemsUseCase.invoke(
            scope = viewModelScope,
            params = FetchSellingMarketItemsUseCase.Params(limit = SELLING_MARKET_ITEMS_LIMIT)
        )
    }.getOrDefault(emptyList())

    private suspend fun fetchMarketHistory() = runCatching {
        fetchMarketHistoryUseCase.invoke(
            scope = viewModelScope,
            params = FetchMarketHistoryUseCase.Params(limit = MARKET_HISTORY_ITEMS_LIMIT)
        )
    }.getOrDefault(emptyList())

    private suspend fun fetchArtCollectibleCategories() =
        getArtCollectibleCategoriesUseCase.invoke(scope = viewModelScope)

    private suspend fun fetchMoreFollowedUsers() =
        getMostFollowedUsersUseCase.invoke(
            scope = viewModelScope,
            params = GetMostFollowedUsersUseCase.Params(limit = MOST_FOLLOWED_USERS_LIMIT)
        )

    private suspend fun fetchMoreLikedTokensUseCase() =
        getMostLikedTokensUseCase.invoke(
            scope = viewModelScope,
            params = GetMostLikedTokensUseCase.Params(limit = MOST_LIKED_TOKENS_LIMIT)
        )

    private suspend fun fetchMoreVisitedTokensUseCase() =
        getMostVisitedTokensUseCase.invoke(
            scope = viewModelScope,
            params = GetMostVisitedTokensUseCase.Params(limit = MOST_VISITED_TOKENS_LIMIT)
        )

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
    val mostFollowedUsers: Iterable<UserInfo> = emptyList(),
    val mostLikedTokens: Iterable<ArtCollectible> = emptyList(),
    val mostVisitedTokens: Iterable<ArtCollectible> = emptyList()
)