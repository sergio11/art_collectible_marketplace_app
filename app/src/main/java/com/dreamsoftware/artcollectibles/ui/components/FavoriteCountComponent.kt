package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible

@Composable
fun FavoriteCountComponent(
    modifier: Modifier = Modifier,
    artCollectible: ArtCollectible? = null,
    defaultColor: Color = Color.Black
) {
    TextWithIcon(
        modifier = modifier,
        tintColor = if (artCollectible?.hasAddedToFav == true) {
            Color.Red
        } else {
            defaultColor
        },
        icon = if (artCollectible?.hasAddedToFav == true) {
            Icons.Filled.Favorite
        } else {
            Icons.Default.FavoriteBorder
        },
        text = artCollectible?.favoritesCount?.toString()
            ?: stringResource(id = R.string.no_text_value_small)
    )
}