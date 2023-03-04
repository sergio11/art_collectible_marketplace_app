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
import androidx.compose.ui.graphics.Brush
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
import com.dreamsoftware.artcollectibles.ui.theme.*

@Composable
fun ArtCollectibleMiniCard(
    context: Context,
    artCollectible: ArtCollectible,
    onClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(262.dp)
            .width(175.dp)
            .border(1.dp, Purple200, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
            .background(brush = Brush.horizontalGradient(
                listOf(Purple80, Purple40)
            ))
            .clickable { onClicked() }
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
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left
            )
            Text(
                artCollectible.metadata.description,
                color = whiteTranslucent,
                fontFamily = montserratFontFamily,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(Modifier.weight(1f))
            FavoriteCountComponent(artCollectible = artCollectible, defaultColor = whiteTranslucent)
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
