package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale

@Composable
fun MarketHistoryMiniCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectibleForSale: ArtCollectibleForSale,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit = {}
) {
    with(artCollectibleForSale) {
        CommonArtCollectibleMiniCard(
            modifier = modifier,
            context = context,
            artCollectible = token,
            reverseStyle = reverseStyle,
            onClicked = onClicked
        ) {

        }
    }
}