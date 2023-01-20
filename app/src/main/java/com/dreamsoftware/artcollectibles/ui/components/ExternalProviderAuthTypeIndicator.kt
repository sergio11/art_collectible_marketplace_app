package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum

@Composable
fun ExternalProviderAuthTypeIndicator(
    modifier: Modifier,
    externalProviderAuthTypeEnum: ExternalProviderAuthTypeEnum
) {
    Image(
        painter = painterResource(
            id = when (externalProviderAuthTypeEnum) {
                ExternalProviderAuthTypeEnum.FACEBOOK -> R.drawable.facebook_icon
                ExternalProviderAuthTypeEnum.GOOGLE -> R.drawable.google_icon_logo
            }
        ),
        contentDescription = "External Provider Icon",
        modifier = modifier
    )
}