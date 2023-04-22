package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.core.CommonAsyncImage

private const val EXTERNAL_ACCOUNT_PERCENTAGE_SIZE = 0.26f

@Composable
fun UserAccountProfilePicture(size: Dp, userInfo: UserInfo? = null, onPictureClicked: () -> Unit = {}) {
    Box {
        CommonAsyncImage(
            modifier =  Modifier
                .size(size)
                .clip(CircleShape)
                .clickable {
                    onPictureClicked()
                },
            context = LocalContext.current,
            imageUrl = userInfo?.photoUrl
        )
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