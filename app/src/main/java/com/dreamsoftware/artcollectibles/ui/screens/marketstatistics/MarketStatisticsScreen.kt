package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserMarketStatistic
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.ChartTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.core.CommonChart
import com.dreamsoftware.artcollectibles.ui.components.core.TopBarAction
import com.google.common.collect.Iterables
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

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
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        MarketStatisticsComponent(
            snackBarHostState = snackBarHostState,
            state = uiState,
            onRefreshData = ::load,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
internal fun MarketStatisticsComponent(
    snackBarHostState: SnackbarHostState,
    state: MarketStatisticsUiState,
    onRefreshData: () -> Unit,
    onBackPressed: () -> Unit,
) {
    with(state) {
        BasicScreen(
            titleRes = R.string.market_statistics_main_title,
            snackBarHostState = snackBarHostState,
            enableVerticalScroll = true,
            centerTitle = true,
            navigationAction = TopBarAction(
                iconRes = R.drawable.back_icon,
                onActionClicked = onBackPressed
            ),
            menuActions = if (!isLoading && hasData()) {
                listOf(TopBarAction(iconRes = R.drawable.refresh_icon, onActionClicked = onRefreshData))
            } else {
                emptyList()
            },
            backgroundContent = {
                ErrorStateNotificationComponent(
                    modifier = Modifier.align(Alignment.Center),
                    isVisible = !isLoading && !hasData(),
                    imageRes = R.drawable.not_data_found,
                    title = stringResource(id = R.string.market_statistics_not_found_message),
                    isRetryButtonVisible = true,
                    onRetryCalled = onRefreshData
                )
            },
            screenContent = {
                LoadingDialog(isShowingDialog = isLoading)
                if (!Iterables.isEmpty(morePurchasesStatistics)) {
                    UserMarketStatisticChart(
                        titleRes = R.string.market_statistics_users_with_more_purchases_title,
                        data = morePurchasesStatistics
                    )
                }
                if (!Iterables.isEmpty(moreSalesStatistics)) {
                    UserMarketStatisticChart(
                        titleRes = R.string.market_statistics_users_with_more_sales_title,
                        data = moreSalesStatistics
                    )
                }
                if (!Iterables.isEmpty(moreTokensCreated)) {
                    UserMarketStatisticChart(
                        titleRes = R.string.market_statistics_users_with_more_tokens_created_title,
                        data = moreTokensCreated
                    )
                }
            }
        )
    }
}

@Composable
private fun UserMarketStatisticChart(
    @StringRes titleRes: Int,
    data: Iterable<UserMarketStatistic>
) {
    StatisticChart(
        type = ChartTypeEnum.COLUMN,
        titleRes = titleRes,
        entryModel = data.mapIndexed { index, userMarketStatistic ->
            TextEntry(
                userMarketStatistic.userInfo.name,
                index.toFloat(),
                userMarketStatistic.value.toFloat()
            )
        }.let { ChartEntryModelProducer(it) }.getModel(),
        bottomAxisValueFormatter = { value, chartValues ->
            (chartValues.chartEntryModel.entries.first()
                .getOrNull(value.toInt()) as? TextEntry)
                ?.label
                .orEmpty()
        }
    )
}

@Composable
private fun StatisticChart(
    type: ChartTypeEnum,
    @StringRes titleRes: Int,
    entryModel: ChartEntryModel,
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>? = null
) {
    Card(
        modifier = Modifier.padding(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(27.dp),
        border = BorderStroke(3.dp, Color.White)
    ) {
        CommonChart(
            modifier = Modifier.padding(8.dp),
            titleRes = titleRes,
            entryModel = entryModel,
            type = type,
            bottomAxisValueFormatter = bottomAxisValueFormatter
        )
    }
}

class TextEntry(
    val label: String,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = TextEntry(label, x, y)
}