package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite


@Composable
fun ArtCollectibleForSaleCard(
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
            Row(
                modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArtCollectiblePrice(
                    iconSize = 15.dp,
                    textSize = 15.sp,
                    priceData = price,
                    textColor = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    }
                )
                Spacer(Modifier.weight(0.9f))
                FavoriteCountComponent(
                    artCollectible = token, defaultColor = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    }
                )
            }
        }
    }
}