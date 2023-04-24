package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketStatisticsViewModel @Inject constructor(): SupportViewModel<MarketStatisticsUiState>() {
    override fun onGetDefaultState(): MarketStatisticsUiState = MarketStatisticsUiState()
}

data class MarketStatisticsUiState(
    val isLoading: Boolean = false
)