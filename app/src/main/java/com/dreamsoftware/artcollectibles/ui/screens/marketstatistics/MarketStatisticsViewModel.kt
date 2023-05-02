package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMarketStatistic
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.google.common.collect.Iterables
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketStatisticsViewModel @Inject constructor(
    private val fetchUsersWithMorePurchasesUseCase: FetchUsersWithMorePurchasesUseCase,
    private val fetchUsersWithMoreSalesUseCase: FetchUsersWithMoreSalesUseCase,
    private val fetchUsersWithMoreTokensCreatedUseCase: FetchUsersWithMoreTokensCreatedUseCase,
    private val fetchMostSoldTokensUseCase: FetchMostSoldTokensUseCase,
    private val fetchMostCancelledTokensUseCase: FetchMostCancelledTokensUseCase
) : SupportViewModel<MarketStatisticsUiState>() {

    private companion object {
        const val MOST_PURCHASES_LIMIT = 5
        const val MOST_SALES_LIMIT = 5
        const val MOST_TOKENS_CREATED_LIMIT = 5
        const val MOST_SOLD_TOKENS_LIMIT = 5
        const val MOST_CANCELLED_TOKENS_LIMIT = 5
    }

    override fun onGetDefaultState(): MarketStatisticsUiState = MarketStatisticsUiState()

    fun load() {
        onLoading()
        viewModelScope.launch {
            runCatching {
                val fetchUsersWithMorePurchasesDeferred = async { fetchUsersWithMorePurchases() }
                val fetchUsersWithMoreSalesDeferred = async { fetchUsersWithMoreSales() }
                val fetchUsersWithMoreTokensCreatedDeferred =
                    async { fetchUsersWithMoreTokensCreated() }
                val fetchMostSoldTokensDeferred = async { fetchMostSoldTokens() }
                val fetchMostCancelledTokensDeferred = async { fetchMostCancelledTokens() }
                val mostPurchases = fetchUsersWithMorePurchasesDeferred.await()
                val mostSales = fetchUsersWithMoreSalesDeferred.await()
                val mostTokensCreated = fetchUsersWithMoreTokensCreatedDeferred.await()
                val mostSoldTokens = fetchMostSoldTokensDeferred.await()
                val mostCancelledTokens = fetchMostCancelledTokensDeferred.await()
                updateState {
                    it.copy(
                        isLoading = false,
                        mostPurchasesChartEntryModel = mostPurchases,
                        mostSalesChartEntryModel = mostSales,
                        mostTokensCreatedChartEntryModel = mostTokensCreated,
                        mostSoldTokensChartEntryModel = mostSoldTokens,
                        mostCancelledTokensChartEntryModel = mostCancelledTokens
                    )
                }
            }.onFailure(::onErrorOccurred)
        }
    }

    private fun onLoading() {
        updateState {
            it.copy(isLoading = true)
        }
    }

    private suspend fun fetchUsersWithMorePurchases(): ChartEntryModel? = runCatching {
        fetchUsersWithMorePurchasesUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMorePurchasesUseCase.Params(limit = MOST_PURCHASES_LIMIT)
        ).let(::mapUserMarketStatisticToChartEntryModel)
    }.getOrNull()

    private suspend fun fetchUsersWithMoreSales(): ChartEntryModel? = runCatching {
        fetchUsersWithMoreSalesUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMoreSalesUseCase.Params(limit = MOST_SALES_LIMIT)
        ).let(::mapUserMarketStatisticToChartEntryModel)
    }.getOrNull()


    private suspend fun fetchUsersWithMoreTokensCreated(): ChartEntryModel? = runCatching {
        fetchUsersWithMoreTokensCreatedUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMoreTokensCreatedUseCase.Params(limit = MOST_TOKENS_CREATED_LIMIT)
        ).let(::mapUserMarketStatisticToChartEntryModel)
    }.getOrNull()

    private suspend fun fetchMostSoldTokens(): ChartEntryModel? = runCatching {
        fetchMostSoldTokensUseCase.invoke(
            scope = viewModelScope,
            params = FetchMostSoldTokensUseCase.Params(limit = MOST_SOLD_TOKENS_LIMIT)
        ).let(::mapArtCollectibleMarketStatisticToChartEntryModel)
    }.getOrNull()

    private suspend fun fetchMostCancelledTokens(): ChartEntryModel? = runCatching {
        fetchMostCancelledTokensUseCase.invoke(
            scope = viewModelScope,
            params = FetchMostCancelledTokensUseCase.Params(limit = MOST_CANCELLED_TOKENS_LIMIT)
        ).let(::mapArtCollectibleMarketStatisticToChartEntryModel)
    }.getOrNull()


    private fun onErrorOccurred(ex: Throwable) {
        ex.printStackTrace()
        updateState {
            it.copy(error = ex)
        }
    }

    private fun mapUserMarketStatisticToChartEntryModel(statisticList: Iterable<UserMarketStatistic>) =
        statisticList.takeIf { !Iterables.isEmpty(it) }?.mapIndexed { index, userMarketStatistic ->
            with(userMarketStatistic) {
                ChartUiTextEntry(
                    label = userInfo.name,
                    x = index.toFloat(),
                    y = value.toFloat()
                )
            }
        }?.let { ChartEntryModelProducer(it) }?.getModel()

    private fun mapArtCollectibleMarketStatisticToChartEntryModel(statisticList: Iterable<ArtCollectibleMarketStatistic>) =
        statisticList.takeIf { !Iterables.isEmpty(it) }?.mapIndexed { index, artCollectibleMarketStatistic ->
            with(artCollectibleMarketStatistic) {
                ChartUiTextEntry(
                    label = artCollectible.displayName,
                    x = index.toFloat(),
                    y = value.toFloat()
                )
            }
        }?.let { ChartEntryModelProducer(it) }?.getModel()
}

data class MarketStatisticsUiState(
    val isLoading: Boolean = false,
    val mostPurchasesChartEntryModel: ChartEntryModel? = null,
    val mostSalesChartEntryModel: ChartEntryModel? = null,
    val mostTokensCreatedChartEntryModel: ChartEntryModel? = null,
    val mostSoldTokensChartEntryModel: ChartEntryModel? = null,
    val mostCancelledTokensChartEntryModel: ChartEntryModel? = null,
    val error: Throwable? = null
) {
    fun hasData() =
                mostPurchasesChartEntryModel != null ||
                mostSalesChartEntryModel != null ||
                mostTokensCreatedChartEntryModel != null ||
                mostSoldTokensChartEntryModel != null ||
                mostCancelledTokensChartEntryModel != null
}

class ChartUiTextEntry(
    val label: String,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = ChartUiTextEntry(label, x, y)
}