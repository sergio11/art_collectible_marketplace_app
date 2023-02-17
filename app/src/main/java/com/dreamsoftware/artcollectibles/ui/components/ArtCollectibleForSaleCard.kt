package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale


@Composable
fun ArtCollectibleForSaleCard(
    context: Context,
    artCollectibleForSale: ArtCollectibleForSale,
    onClicked: () -> Unit
) {
    with(artCollectibleForSale) {
        Column(
            modifier = Modifier
                .height(262.dp)
                .width(175.dp)
                .border(
                    1.dp,
                    Color.White.copy(0.5f),
                    RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White.copy(0.2f))
                .clickable { onClicked() }
        ) {
            ArtCollectibleForSaleImage(context, token)
            Column(
                Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    token.name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Left
                )
                Text(
                    token.description,
                    color = Color(235, 235, 245).copy(0.6f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left
                )
            }
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.matic_icon),
                        contentDescription = "Ethereum Icon",
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        price.toString(),
                        color = Color.White,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Left
                    )
                }
                Spacer(Modifier.weight(1f))
                FavoriteCountComponent(artCollectible = token)
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
            .data(artCollectible.imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.user_placeholder),
        contentDescription = stringResource(R.string.image_content_description),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(155.dp)
            .width(155.dp)
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
