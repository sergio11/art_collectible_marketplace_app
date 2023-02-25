package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tintColor: Color = Color.Black,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            tint = tintColor,
            imageVector = icon,
            contentDescription = "Icon",
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