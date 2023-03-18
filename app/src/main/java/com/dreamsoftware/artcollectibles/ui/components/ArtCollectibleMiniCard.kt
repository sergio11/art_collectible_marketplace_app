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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun ArtCollectibleMiniCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectible: ArtCollectible,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(300.dp)
            .width(200.dp)
            .border(
                2.dp, if (reverseStyle) {
                    Color.White
                } else {
                    Purple200
                }, RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(
                color = if (reverseStyle) {
                    Purple200
                } else {
                    Color.White.copy(alpha = 0.9f)
                }
            )
            .clickable { onClicked() }
            .then(modifier)
    ) {
        ArtCollectibleImage(context, artCollectible)
        Column(
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp)
        ) {
            Text(
                artCollectible.displayName,
                fontFamily = montserratFontFamily,
                color = if (reverseStyle) {
                    Color.White
                } else {
                    Color.Black
                },
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left
            )
            Text(
                artCollectible.metadata.description,
                color = if (reverseStyle) {
                    Color.White
                } else {
                    Color.Black
                },
                fontFamily = montserratFontFamily,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        TextWithImage(
            modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 4.dp),
            imageRes = R.drawable.token_category_icon,
            text = artCollectible.metadata.category.name,
            tintColor = if (reverseStyle) {
                Color.White
            } else {
                Color.Black
            }
        )
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
                    Color.White
                } else {
                    Color.Black
                },
                text = artCollectible.visitorsCount.toString()
            )
            TextWithImage(
                modifier = Modifier.padding(8.dp),
                imageRes = R.drawable.comments_icon,
                tintColor = if (reverseStyle) {
                    Color.White
                } else {
                    Color.Black
                },
                text = artCollectible.commentsCount.toString()
            )
            FavoriteCountComponent(artCollectible = artCollectible, defaultColor = if (reverseStyle) {
                Color.White
            } else {
                Color.Black
            })
        }
    }
}

@Composable
private fun ArtCollectibleImage(
    context: Context,
    artCollectible: ArtCollectible
) {
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(artCollectible.metadata.imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.user_placeholder),
        contentDescription = stringResource(R.string.image_content_description),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(155.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(27.dp)
            )
            .fillMaxSize()
            .clip(RoundedCornerShape(27.dp))
    )
}
