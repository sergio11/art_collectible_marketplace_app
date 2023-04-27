package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.ChartTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.core.CommonChart
import com.dreamsoftware.artcollectibles.ui.components.core.TopBarAction
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryModelOf

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
            ),
            menuActions = listOf(
                TopBarAction(iconRes = R.drawable.refresh_icon) {}
            ),
            screenContent = {

                StatisticChart(
                    type = ChartTypeEnum.COLUMN,
                    titleRes = R.string.token_detail_price_history_title_text,
                    entryModel = entryModelOf(4f, 12f, 8f, 16f, 20f, 30f, 5f, 10f)
                )

                val chartTextEntryModelProducer = listOf(
                    "Sergio11" to 2f,
                    "Martin" to 4f,
                    "Pedro" to 2f,
                    "Nico" to 8f,
                )
                    .mapIndexed { index, (user, y) -> TextEntry(user, index.toFloat(), y) }
                    .let { ChartEntryModelProducer(it) }

                StatisticChart(
                    type = ChartTypeEnum.COLUMN,
                    titleRes = R.string.token_detail_price_history_title_text,
                    entryModel = chartTextEntryModelProducer.getModel(),
                    bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                        (chartValues.chartEntryModel.entries.first().getOrNull(value.toInt()) as? TextEntry)
                            ?.label
                            .orEmpty()
                    }
                )
            }
        )
    }
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