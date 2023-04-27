package com.dreamsoftware.artcollectibles.ui.components.core

import android.graphics.Typeface
import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModel

enum class ChartTypeEnum {
    LINE,
    COLUMN
}

@Composable
fun CommonChart(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    entryModel: ChartEntryModel,
    type: ChartTypeEnum = ChartTypeEnum.LINE,
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>? = null
) {
    Column(
        modifier = modifier
    ) {
        CommonText(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            type = CommonTextTypeEnum.TITLE_LARGE,
            titleRes = titleRes,
            singleLine = true
        )
        Chart(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            chart = when (type) {
                ChartTypeEnum.LINE -> lineChart()
                ChartTypeEnum.COLUMN -> columnChart()
            },
            model = entryModel,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter ?: DecimalFormatAxisValueFormatter(),
                label = textComponent {
                    color = Purple700.toArgb()
                    textSizeSp = 12f
                    typeface = Typeface.MONOSPACE
                    ellipsize = TextUtils.TruncateAt.END
                },
                title = stringResource(id = titleRes),
                titleComponent = textComponent {
                    color = Purple700.toArgb()
                    textSizeSp = 20f
                    typeface = Typeface.MONOSPACE
                    ellipsize = TextUtils.TruncateAt.END
                }
            )
        )
    }
}