package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.core.CommonAsyncImage
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun MarketItemImage(
    context: Context,
    marketItem: ArtCollectibleForSale,
    reverseStyle: Boolean = false
) {
    with(marketItem) {
        Box {
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
                imageUrl = marketItem.token.metadata.imageUrl
            )
            Image(
                painter = painterResource(id = if (sold) {
                    R.drawable.sold_market_items
                } else if (canceled) {
                    R.drawable.cancelled_market_items
                } else {
                    R.drawable.available_market_items
                }),
                colorFilter = ColorFilter.tint(
                    if (sold) {
                        Color.Green.copy(0.6f)
                    } else if (canceled) {
                        Color.Red.copy(0.6f)
                    } else {
                        Color.Yellow.copy(0.6f)
                    }
                ),
                contentDescription = stringResource(R.string.image_content_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(155.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(if (sold) {
                        Color.Green.copy(0.4f)
                    } else if (canceled) {
                        Color.Red.copy(0.4f)
                    } else {
                        Color.Yellow.copy(0.4f)
                    })
            )
        }
    }
}
