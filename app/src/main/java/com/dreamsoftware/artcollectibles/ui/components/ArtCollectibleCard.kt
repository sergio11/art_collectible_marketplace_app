package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible

@Composable
fun ArtCollectibleCard(
    modifier: Modifier,
    context: Context,
    artCollectible: ArtCollectible
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(27.dp),
        border = BorderStroke(3.dp, Color.White),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            ArtCollectibleImage(context, artCollectible)
            ArtCollectibleMiniInfoComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                showPreviewDescription = true,
                artCollectible
            )
        }
    }
}


@Composable
private fun ArtCollectibleImage(
    context: Context,
    artCollectible: ArtCollectible
) {
    val imageDefaultModifier = Modifier
        .height(155.dp)
        .fillMaxWidth()
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(artCollectible.imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.user_placeholder),
        contentDescription = stringResource(R.string.image_content_description),
        contentScale = ContentScale.Crop,
        modifier = imageDefaultModifier
    )
}