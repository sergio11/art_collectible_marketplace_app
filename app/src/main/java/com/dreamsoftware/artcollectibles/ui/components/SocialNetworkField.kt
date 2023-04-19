package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R

@Composable
fun SocialNetworkField(
    @StringRes labelRes: Int,
    @StringRes placeHolderRes: Int,
    value: String? = null,
    onValueChanged: (String) -> Unit = {}
) {
    CommonDefaultTextField(
        labelRes = labelRes,
        placeHolderRes = placeHolderRes,
        value = value.orEmpty(),
        onValueChanged = onValueChanged,
        leadingIcon = {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.instagram_icon),
                contentDescription = stringResource(R.string.image_content_description),
            )
        }
    )
}