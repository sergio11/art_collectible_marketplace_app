package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.Purple80

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(6.dp)
            .then(modifier),
        color = Purple80
    ) {
        IconToggleButton(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        ) {
            Icon(
                tint = Color(0xffE91E63),
                modifier = Modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        scaleX = 1.3f
                        scaleY = 1.3f
                    },
                imageVector = if (isChecked) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }
    }
}