package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo

private const val EXTERNAL_ACCOUNT_PERCENTAGE_SIZE = 0.26f

@Composable
fun UserAccountProfilePicture(size: Dp, userInfo: UserInfo? = null, onPictureClicked: () -> Unit = {}) {
    Box {
        val profilePictureModifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                onPictureClicked()
            }
        userInfo?.photoUrl?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.user_placeholder),
                contentDescription = stringResource(R.string.image_content_description),
                contentScale = ContentScale.Crop,
                modifier = profilePictureModifier
            )
        } ?: run {
            Image(
                painter = painterResource(R.drawable.user_placeholder),
                contentDescription = "User Placeholder",
                contentScale = ContentScale.Crop,
                modifier = profilePictureModifier
            )
        }
        userInfo?.externalProviderAuthType?.let {
            ExternalProviderAuthTypeIndicator(
                modifier = Modifier
                    .size(Dp(size.value * EXTERNAL_ACCOUNT_PERCENTAGE_SIZE))
                    .zIndex(2f)
                    .align(Alignment.BottomEnd),
                externalProviderAuthTypeEnum = it
            )
        }
    }
}