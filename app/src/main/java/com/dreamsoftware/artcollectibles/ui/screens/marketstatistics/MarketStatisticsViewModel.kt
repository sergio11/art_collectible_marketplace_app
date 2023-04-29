package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchUsersWithMorePurchasesUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchUsersWithMoreSalesUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchUsersWithMoreTokensCreatedUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.google.common.collect.Iterables
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
                        morePurchasesStatistics = morePurchases,
                        moreSalesStatistics = moreSales,
                        moreTokensCreated = moreTokensCreated
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

    private suspend fun fetchUsersWithMorePurchases() =
        fetchUsersWithMorePurchasesUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMorePurchasesUseCase.Params(limit = MORE_PURCHASES_LIMIT)
        )

    private suspend fun fetchUsersWithMoreSales() =
        fetchUsersWithMoreSalesUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMoreSalesUseCase.Params(limit = MORE_SALES_LIMIT)
        )

    private suspend fun fetchUsersWithMoreTokensCreated() =
        fetchUsersWithMoreTokensCreatedUseCase.invoke(
            scope = viewModelScope,
            params = FetchUsersWithMoreTokensCreatedUseCase.Params(limit = MORE_TOKENS_CREATED_LIMIT)
        )

    private fun onErrorOccurred(ex: Throwable) {
        ex.printStackTrace()
        updateState {
            it.copy(error = ex)
        }
    }

}

data class MarketStatisticsUiState(
    val isLoading: Boolean = false,
    val morePurchasesStatistics: Iterable<UserMarketStatistic> = emptyList(),
    val moreSalesStatistics: Iterable<UserMarketStatistic> = emptyList(),
    val moreTokensCreated: Iterable<UserMarketStatistic> = emptyList(),
    val error: Throwable? = null
) {
    fun hasData() =
        !Iterables.isEmpty(morePurchasesStatistics) ||
                !Iterables.isEmpty(moreSalesStatistics) ||
                !Iterables.isEmpty(moreTokensCreated)
}