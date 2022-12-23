package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun ArtCollectibleCategoryCard(title: String, image: Painter) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(27.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = RoundedCornerShape(27.dp)
            )
            .height(186.dp)
            .width(280.dp)
    ) {
        Image(
            painter = image,
            contentDescription = "Card Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Preview(widthDp = 200)
@Composable
fun PreviewArtCollectibleCategoryCard() {
    ArtCollectibleMarketplaceTheme {
        Column() {
            ArtCollectibleCategoryCard(
                title = "Music",
                image = painterResource(id = R.drawable.card_music)
            )
            ArtCollectibleCategoryCard(
                title = "Art",
                image = painterResource(id = R.drawable.card_art)
            )
            ArtCollectibleCategoryCard(
                title = "Wave",
                image = painterResource(id = R.drawable.card_wave)
            )
        }
    }
}

