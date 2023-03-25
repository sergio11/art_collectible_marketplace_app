package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigInteger

private val DEFAULT_TEXT_SIZE = 13.sp
private val DEFAULT_TEXT_COLOR = Color.White
private val DEFAULT_ICON_SIZE = 13.dp

@Composable
fun ArtCollectiblePrice(
    modifier: Modifier = Modifier,
    iconSize: Dp = DEFAULT_ICON_SIZE,
    textSize: TextUnit = DEFAULT_TEXT_SIZE,
    textColor: Color = DEFAULT_TEXT_COLOR,
    price: BigInteger
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MaticIconComponent(size = iconSize)
        Text(
            price.toString(),
            color = textColor,
            fontSize = textSize,
            textAlign = TextAlign.Left
        )
    }
}