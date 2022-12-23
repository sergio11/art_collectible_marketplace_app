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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
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
import com.dreamsoftware.artcollectibles.domain.models.nfts
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme


@Composable
fun ArtCollectibleCard(
    title: String,
    subtitle: String,
    image: Painter,
    likes: Int,
    eth: Double
) {
    var isLiked by remember { mutableStateOf(false) }

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
    ) {
        Image(
            painter = image,
            contentDescription = "",
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
                .clip(RoundedCornerShape(27.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp)
        ) {
            Text(
                title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Left
            )
            Text(
                subtitle,
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
                    painter = painterResource(id = R.drawable.icon_eth),
                    contentDescription = "Ethereum Icon",
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    eth.toString(),
                    color = Color.White,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Left
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(checked = isLiked, onCheckedChange = {
                    isLiked = !isLiked
                }, modifier = Modifier.size(13.dp)) {
                    Icon(
                        tint = if (isLiked) { Color.Red } else { Color(235, 235, 245).copy(0.6f) },
                        imageVector = if (isLiked) { Icons.Filled.Favorite } else { Icons.Default.FavoriteBorder },
                        contentDescription = "Favorite Button"
                    )
                }
                Text(
                    likes.toString(),
                    color = Color(235, 235, 245).copy(0.6f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewArtCollectibleCard() {
    ArtCollectibleMarketplaceTheme {
        ArtCollectibleCard(
            title = nfts[0].title,
            subtitle = nfts[0].subTitle,
            image = painterResource(id = nfts[0].image),
            likes = nfts[0].likes,
            eth = nfts[0].eth
        )
    }
}
