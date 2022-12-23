package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.domain.models.collections
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun ArtCollectibleCollectionCard(title: String, image: Painter, likes: Int) {

    var isLiked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(216.dp)
            .height(216.dp)
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(0.2f))
    ) {
        Image(
            image,
            contentDescription = "NFT Image",
            modifier = Modifier
                .height(155.dp)
                .width(155.dp)
                .padding(10.dp)
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(22.dp)
                )
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp))
        )
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left

            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(checked = isLiked, onCheckedChange = {
                    isLiked = !isLiked
                }, modifier = Modifier.size(13.dp)) {
                    Icon(
                        tint = if (isLiked) Color.Red else Color(235, 235, 245).copy(0.6f),
                        imageVector = if (isLiked)
                            Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Button"
                    )
                }
                Text(
                    likes.toString(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Right,
                    color = Color(235, 235, 245).copy(0.6f)
                )
            }
        }
    }

}


@Composable
@Preview
fun PreviewCollectionCard() {
    ArtCollectibleMarketplaceTheme {
        ArtCollectibleCollectionCard(
            collections[0].title,
            painterResource(id = collections[0].image),
            collections[0].likes
        )
    }
}