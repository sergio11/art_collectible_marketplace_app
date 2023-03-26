package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.theme.*


@Composable
fun ArtCollectibleForSaleCard(
    modifier: Modifier = Modifier,
    context: Context,
    artCollectibleForSale: ArtCollectibleForSale,
    reverseStyle: Boolean = false,
    onClicked: () -> Unit
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
            ArtCollectibleForSaleImage(context, token)
            Column(
                Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    token.metadata.name,
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
                Text(
                    token.metadata.description,
                    color = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    },
                    fontFamily = montserratFontFamily,
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                TextWithImage(
                    modifier = Modifier.padding(top = 8.dp),
                    imageRes = R.drawable.token_category_icon,
                    text = token.metadata.category.name,
                    tintColor = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    }
                )
            }
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArtCollectiblePrice(
                    iconSize = 15.dp,
                    textSize = 15.sp,
                    price = price,
                    textColor = if (reverseStyle) {
                        BackgroundWhite
                    } else {
                        Color.Black
                    }
                )
                Spacer(Modifier.weight(1f))
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

@Composable
private fun ArtCollectibleForSaleImage(
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
