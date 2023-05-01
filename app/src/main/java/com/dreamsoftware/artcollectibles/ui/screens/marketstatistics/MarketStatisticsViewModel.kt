package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchUsersWithMorePurchasesUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchUsersWithMoreSalesUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchUsersWithMoreTokensCreatedUseCase
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
    private val fetchUsersWithMoreTokensCreatedUseCase: FetchUsersWithMoreTokensCreatedUseCase
) : SupportViewModel<MarketStatisticsUiState>() {

    private companion object {
        const val MORE_PURCHASES_LIMIT = 5
        const val MORE_SALES_LIMIT = 5
        const val MORE_TOKENS_CREATED_LIMIT = 5
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
                val morePurchases = fetchUsersWithMorePurchasesDeferred.await()
                val moreSales = fetchUsersWithMoreSalesDeferred.await()
                val moreTokensCreated = fetchUsersWithMoreTokensCreatedDeferred.await()
                updateState {
                    it.copy(
                        isLoading = false,
                        morePurchasesChartEntryModel = morePurchases,
                        moreSalesChartEntryModel = moreSales,
                        moreTokensCreatedChartEntryModel = moreTokensCreated
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
            params = FetchUsersWithMorePurchasesUseCase.Params(limit = MORE_PURCHASES_LIMIT)
        ).let(::mapToChartEntryModel)
    }.getOrNull()

    private suspend fun fetchUsersWithMoreSales(): ChartEntryModel? = runCatching {
        fetchUsersWithMoreSalesUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMoreSalesUseCase.Params(limit = MORE_SALES_LIMIT)
        ).let(::mapToChartEntryModel)
    }.getOrNull()


    private suspend fun fetchUsersWithMoreTokensCreated(): ChartEntryModel? = runCatching {
        fetchUsersWithMoreTokensCreatedUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMoreTokensCreatedUseCase.Params(limit = MORE_TOKENS_CREATED_LIMIT)
        ).let(::mapToChartEntryModel)
    }.getOrNull()

    private fun onErrorOccurred(ex: Throwable) {
        ex.printStackTrace()
        updateState {
            it.copy(error = ex)
        }
    }

    private fun mapToChartEntryModel(data: Iterable<UserMarketStatistic>) =
        data.mapIndexed { index, userMarketStatistic ->
            with(userMarketStatistic) {
                ChartUiTextEntry(
                    label = userInfo.name,
                    x = index.toFloat(),
                    y = value.toFloat()
                )
            }
        }.let { ChartEntryModelProducer(it) }.getModel()
}

data class MarketStatisticsUiState(
    val isLoading: Boolean = false,
    val morePurchasesChartEntryModel: ChartEntryModel? = null,
    val moreSalesChartEntryModel: ChartEntryModel? = null,
    val moreTokensCreatedChartEntryModel: ChartEntryModel? = null,
    val error: Throwable? = null
) {
    fun hasData() =
        morePurchasesChartEntryModel != null ||
                moreSalesChartEntryModel != null ||
                moreTokensCreatedChartEntryModel != null
}

class ChartUiTextEntry(
    val label: String,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = ChartUiTextEntry(label, x, y)
}