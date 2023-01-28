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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun UserInfoArtistCard(
    modifier: Modifier,
    context: Context,
    user: UserInfo
) {
    var infoCardPalette by remember {
        mutableStateOf(
            ContextCompat.getDrawable(context, R.drawable.user_placeholder)?.toBitmapOrNull(40, 40)
                ?.let {
                    Palette.from(it).generate()
                }
        )
    }
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(infoCardPalette?.darkVibrantSwatch?.rgb?.let {
            Color(it)
        } ?: Color.White),
        shape = RoundedCornerShape(27.dp),
        border = BorderStroke(3.dp, Color.White),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // User Profile Image
            UserProfileImage(context, user) {
                infoCardPalette = it
            }
            // User Profile Detail
            UserProfileDetail(context, user, infoCardPalette)
        }
    }
}

@Composable
private fun UserProfileImage(
    context: Context,
    user: UserInfo,
    onPaletteGenerated: (Palette) -> Unit
) {
    Box {
        val imageDefaultModifier = Modifier
            .height(155.dp)
            .fillMaxWidth()
        with(user) {
            photoUrl?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it)
                        // Disable hardware bitmaps as Palette needs to read the image's pixels.
                        .allowHardware(false)
                        .crossfade(true)
                        .listener(onSuccess = { _, result ->
                            // Create the palette on a background thread.
                            Palette.Builder(result.drawable.toBitmap()).generate { palette ->
                                palette?.let(onPaletteGenerated)
                            }
                        })
                        .build(),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentDescription = stringResource(R.string.image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = imageDefaultModifier
                )
            } ?: run {
                Image(
                    painter = painterResource(R.drawable.user_placeholder),
                    contentDescription = "User Placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = imageDefaultModifier
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
