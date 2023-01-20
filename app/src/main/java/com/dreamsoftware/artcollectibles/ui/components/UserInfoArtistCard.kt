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
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun UserInfoArtistCard(
    modifier: Modifier,
    context: Context,
    user: UserInfo
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(27.dp),
        border = BorderStroke(3.dp, Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // User Profile Image
            UserProfileImage(user)

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = user.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                user.info?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
internal fun UserProfileImage(user: UserInfo) {
    Box {
        val imageDefaultModifier = Modifier
            .height(155.dp)
            .fillMaxWidth()
        with(user) {
            photoUrl?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentDescription = stringResource(R.string.image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = imageDefaultModifier,
                    onSuccess = {

                        Palette.from()
                        it.painter.
                        /*Palette.from(bitmap).generate()
                        it.painter*/
                    }
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
                info = ".....",
                contact = "dreamsoftware92@gmail.com",
                walletAddress = "x0343434343434"
            )
        )
    }
}
