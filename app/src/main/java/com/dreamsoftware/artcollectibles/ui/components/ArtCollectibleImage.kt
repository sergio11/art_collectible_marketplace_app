package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.core.CommonAsyncImage
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun ArtCollectibleImage(
    context: Context,
    artCollectible: ArtCollectible,
    reverseStyle: Boolean = false
) {
    CommonAsyncImage(
        modifier = Modifier
            .height(155.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                2.dp,
                color = if (reverseStyle) {
                    BackgroundWhite
                } else {
                    Purple40
                },
                shape = RoundedCornerShape(27.dp)
            )
            .clip(RoundedCornerShape(27.dp)),
        context = context,
        imageUrl = artCollectible.metadata.imageUrl
    )
}
