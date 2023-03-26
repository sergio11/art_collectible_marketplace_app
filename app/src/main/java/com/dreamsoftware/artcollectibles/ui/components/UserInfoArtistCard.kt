package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple200

@Composable
fun UserInfoArtistCard(
    modifier: Modifier,
    context: Context,
    user: UserInfo,
    reverseStyle: Boolean = false,
) {
    Card(
        modifier = Modifier
            .height(300.dp)
            .width(190.dp)
            .then(modifier),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(if (reverseStyle) {
            Purple200
        } else {
            BackgroundWhite
        }),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(3.dp, if (reverseStyle) {
            BackgroundWhite
        } else {
            Purple200
        }),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Profile Image
            UserProfileImage(
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
                    .clip(RoundedCornerShape(27.dp)),
                context = context,
                user = user
            )
            // User Profile Detail
            UserMiniInfoComponent(
                modifier = Modifier
                    .padding(4.dp),
                userInfo = user,
                showPicture = false
            )
            UserFollowersInfoComponent(
                modifier = Modifier
                    .padding(4.dp),
                smallSize = true,
                followersCount = user.followers,
                followingCount = user.following
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
