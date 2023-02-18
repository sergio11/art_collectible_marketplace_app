package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible

@Composable
fun VisitorsCountComponent(
    modifier: Modifier = Modifier,
    artCollectible: ArtCollectible?,
    defaultColor: Color = Color.Black
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            tint = defaultColor,
            imageVector = if ((artCollectible?.visitorsCount ?: 0) > 0) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            },
            contentDescription = "Visitors count"
        )
        Text(
            artCollectible?.visitorsCount?.toString()
                ?: stringResource(id = R.string.no_text_value_small),
            color = defaultColor,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            textAlign = TextAlign.Right
        )
    }
}