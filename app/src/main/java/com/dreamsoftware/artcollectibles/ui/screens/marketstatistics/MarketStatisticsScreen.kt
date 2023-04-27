package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.TopBarAction

@Composable
fun MarketStatisticsScreen(
    viewModel: MarketStatisticsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = MarketStatisticsUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val context = LocalContext.current
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
        }
        MarketStatisticsComponent(
            context = context,
            state = uiState,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
internal fun MarketStatisticsComponent(
    context: Context,
    state: MarketStatisticsUiState,
    onBackPressed: () -> Unit,
) {
    with(state) {
        BasicScreen(
            titleRes = R.string.market_statistics_main_title,
            enableVerticalScroll = true,
            centerTitle = true,
            navigationAction = TopBarAction(
                iconRes = R.drawable.back_icon,
                onActionClicked = onBackPressed
            )
        ) {

        }
    }
}