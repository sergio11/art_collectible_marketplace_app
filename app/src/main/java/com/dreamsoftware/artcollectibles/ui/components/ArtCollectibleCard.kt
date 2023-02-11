package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

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
            ArtCollectibleDetail(artCollectible)
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

@Composable
private fun ArtCollectibleDetail(
    artCollectible: ArtCollectible
) {
    with(artCollectible) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "#$id - $name",
                fontFamily = montserratFontFamily,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            TokenRoyaltyComponent(
                modifier = Modifier.padding(top = 8.dp),
                royalty
            )
            Text(
                text = description,
                maxLines = 3,
                fontFamily = montserratFontFamily,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}