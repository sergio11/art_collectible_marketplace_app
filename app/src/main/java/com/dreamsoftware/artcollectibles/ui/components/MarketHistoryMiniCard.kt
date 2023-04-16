package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun MarketHistoryMiniCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectibleForSale: ArtCollectibleForSale,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit = {}
) {
    with(artCollectibleForSale) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .width(190.dp)
                .border(
                    2.dp, if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Purple200
                    }, RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp))
                .background(
                    color = if (reverseStyle) {
                        Purple200
                    } else {
                        BackgroundWhite
                    }
                )
                .clickable { onClicked() }
                .then(modifier)
        ) {
            MarketItemImage(context, artCollectibleForSale)
            Column(
                Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    token.displayName,
                    fontFamily = montserratFontFamily,
                    color = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    },
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}