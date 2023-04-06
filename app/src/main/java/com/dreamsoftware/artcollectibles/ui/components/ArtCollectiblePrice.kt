package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectiblePrices
import com.dreamsoftware.artcollectibles.ui.extensions.format

private val DEFAULT_TEXT_SIZE = 13.sp
private val DEFAULT_TEXT_COLOR = Color.White
private val DEFAULT_ICON_SIZE = 13.dp

@Composable
fun ArtCollectiblePrice(
    modifier: Modifier = Modifier,
    iconSize: Dp = DEFAULT_ICON_SIZE,
    textSize: TextUnit = DEFAULT_TEXT_SIZE,
    textColor: Color = DEFAULT_TEXT_COLOR,
    fullMode: Boolean = false,
    priceData: ArtCollectiblePrices?
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MaticIconComponent(size = iconSize)
            Text(
                priceData?.priceInEth?.toString() ?: stringResource(id = R.string.no_text_value_small),
                color = textColor,
                fontSize = textSize,
                textAlign = TextAlign.Left
            )
        }
        if(fullMode) {
            priceData?.priceInEUR?.let {
                TextWithImage(
                    modifier = Modifier.padding(8.dp),
                    imageRes = R.drawable.euro_icon,
                    text = it.format(),
                    iconSize = iconSize
                )
            }
            priceData?.priceInUSD?.let {
                TextWithImage(
                    modifier = Modifier.padding(8.dp),
                    imageRes = R.drawable.usd_icon,
                    text = it.format(),
                    iconSize = iconSize
                )
            }
        }
    }
}