package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.core.CommonArtCollectibleMiniCard
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite

@Composable
fun ArtCollectibleMiniCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectible: ArtCollectible,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit = {}
) {
    CommonArtCollectibleMiniCard(
        modifier = modifier,
        context = context,
        artCollectible = artCollectible,
        reverseStyle = reverseStyle,
        onClicked = onClicked
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextWithIcon(
                icon = if (artCollectible.visitorsCount > 0) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                tintColor = if (reverseStyle) {
                    BackgroundWhite
                } else {
                    Color.Black
                },
                text = artCollectible.visitorsCount.toString()
            )
            TextWithImage(
                imageRes = R.drawable.comments_icon,
                tintColor = if (reverseStyle) {
                    BackgroundWhite
                } else {
                    Color.Black
                },
                text = artCollectible.commentsCount.toString()
            )
            FavoriteCountComponent(artCollectible = artCollectible, defaultColor = if (reverseStyle) {
                BackgroundWhite
            } else {
                Color.Black
            })
        }
    }
}