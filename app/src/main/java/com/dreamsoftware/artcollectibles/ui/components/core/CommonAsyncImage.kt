package com.dreamsoftware.artcollectibles.ui.components.core

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.BackgroundWhite
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun CommonAsyncImage(
    modifier: Modifier,
    context: Context,
    reverseStyle: Boolean = false,
    imageUrl: String?,
    colorFilter: ColorFilter? = null
) {
    imageUrl?.let {
        SubcomposeAsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(context)
                .data(it)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.image_content_description),
            colorFilter = colorFilter
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                DefaultImagePlaceholder(modifier, reverseStyle)
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    } ?: run {
        DefaultImagePlaceholder(modifier, reverseStyle)
    }
}

@Composable
private fun DefaultImagePlaceholder(
    modifier: Modifier,
    reverseStyle: Boolean = false
) {
    Image(
        painter = painterResource(R.drawable.default_image_placeholder),
        colorFilter = ColorFilter.tint(if(reverseStyle) {
            BackgroundWhite
        } else {
            Purple40
        }),
        contentDescription = stringResource(R.string.image_content_description),
        contentScale = ContentScale.Fit,
        modifier = modifier.background(if(reverseStyle) {
            Purple40
        } else {
            BackgroundWhite
        })
    )
}