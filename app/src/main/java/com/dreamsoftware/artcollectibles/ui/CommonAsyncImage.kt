package com.dreamsoftware.artcollectibles.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun CommonAsyncImage(
    modifier: Modifier,
    context: Context,
    imageUrl: String?
) {
    imageUrl?.let {
        AsyncImage(
            modifier = Modifier.then(modifier),
            model = ImageRequest.Builder(context)
                .data(it)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.default_image_placeholder),
            placeholder = painterResource(R.drawable.default_image_placeholder),
            contentDescription = stringResource(R.string.image_content_description),
            contentScale = ContentScale.FillBounds
        )
    } ?: run {
        Image(
            painter = painterResource(R.drawable.default_image_placeholder),
            contentDescription = stringResource(R.string.image_content_description),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.background(Purple40).then(modifier)
        )
    }
}