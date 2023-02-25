package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun TextWithImage(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    tintColor: Color = Color.Black,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(20.dp)
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = text,
            color = tintColor,
            fontFamily = montserratFontFamily,
            style = MaterialTheme.typography.titleMedium
        )
    }
}