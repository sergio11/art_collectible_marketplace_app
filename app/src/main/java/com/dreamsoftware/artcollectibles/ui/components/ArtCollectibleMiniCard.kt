package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

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
            modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextWithIcon(
                modifier = Modifier.padding(8.dp),
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
                modifier = Modifier.padding(8.dp),
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