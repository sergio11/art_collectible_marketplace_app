package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.dreamsoftware.artcollectibles.ui.theme.whiteTranslucent

@Composable
fun UserInfoArtistCard(
    modifier: Modifier,
    context: Context,
    user: UserInfo
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(27.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = RoundedCornerShape(27.dp)
            )
            .height(186.dp)
            .width(280.dp)
            .then(modifier),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(27.dp),
        border = BorderStroke(3.dp, Color.White),
    ) {

        Box {
            // User Profile Image
            UserProfileImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                context = context,
                user = user
            )
            // User Profile Detail
            UserMiniInfoComponent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = whiteTranslucent, shape = RectangleShape)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                userInfo = user,
                showPicture = false
            )
        }
    }
}

@Composable
private fun UserProfileImage(
    modifier: Modifier,
    context: Context,
    user: UserInfo
) {
    Box {
        with(user) {
            photoUrl?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentDescription = stringResource(R.string.image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                )
            } ?: run {
                Image(
                    painter = painterResource(R.drawable.user_placeholder),
                    contentDescription = "User Placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                )
            }
            externalProviderAuthType?.let {
                ExternalProviderAuthTypeIndicator(
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .padding(end = 8.dp)
                        .zIndex(2f)
                        .align(Alignment.BottomEnd),
                    externalProviderAuthTypeEnum = it
                )
            }
        }
    }
}

@Composable
internal fun UserProfileDetail(context: Context, user: UserInfo, palette: Palette?) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = user.name.ifBlank {
                context.getString(R.string.search_user_info_name_empty)
            },
            textAlign = TextAlign.Center,
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelLarge,
            color = palette?.lightVibrantSwatch?.rgb?.let { paletteColor ->
                Color(paletteColor)
            } ?: Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if (user.info.isNullOrBlank()) {
                context.getString(R.string.search_user_info_description_empty)
            } else {
                user.info
            },
            textAlign = TextAlign.Center,
            maxLines = 3,
            fontFamily = montserratFontFamily,
            overflow = TextOverflow.Ellipsis,
            color = palette?.lightMutedSwatch?.rgb?.let { paletteColor ->
                Color(paletteColor)
            } ?: Color.DarkGray,
            modifier = Modifier
                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Preview
@Composable
fun PreviewUserInfoCard() {
    ArtCollectibleMarketplaceTheme {
        UserInfoArtistCard(
            modifier = Modifier
                .height(262.dp)
                .width(175.dp),
            context = LocalContext.current,
            user = UserInfo(
                uid = "1232REDX",
                name = "Sergio Sánchez",
                info = "Esto es una descripción de prueba ...",
                contact = "mi_email@gmail.com",
                walletAddress = "x0343434343434"
            )
        )
    }
}
