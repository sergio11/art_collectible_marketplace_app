package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun EditButton(
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit
) {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .then(modifier),
        color = BackgroundWhite
    ) {
        IconButton(
            onClick = onEditClicked
        ) {
            Icon(
                tint = Purple40,
                modifier = Modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        scaleX = 1.3f
                        scaleY = 1.3f
                    },
                imageVector = Icons.Filled.Edit,
                contentDescription = null
            )
        }
    }
}