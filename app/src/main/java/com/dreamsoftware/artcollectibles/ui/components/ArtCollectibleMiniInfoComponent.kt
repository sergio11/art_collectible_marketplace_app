package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

private const val DEFAULT_LIMIT_DESC_MAX_LINES = 3

@Composable
fun ArtCollectibleMiniInfoComponent(
    modifier: Modifier = Modifier,
    showPreviewDescription: Boolean = false,
    artCollectible: ArtCollectible?
) {
    Column(modifier = modifier) {
        Text(
            text = artCollectible?.metadata?.name ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = artCollectible?.metadata?.createdAt?.format()?.let {
                stringResource(id = R.string.token_detail_created_at_label, it)
            } ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall
        )
        Row {
            TextWithImage(
                modifier = Modifier.padding(8.dp),
                imageRes = R.drawable.token_creator_icon,
                text = artCollectible?.author?.name ?: stringResource(id = R.string.no_text_value)
            )
            TextWithImage(
                modifier = Modifier.padding(8.dp),
                imageRes = R.drawable.crown,
                text = artCollectible?.royalty?.let { "$it%" } ?: stringResource(id = R.string.no_text_value)
            )
            FavoriteCountComponent(
                modifier = Modifier.padding(8.dp),
                artCollectible = artCollectible
            )
            TextWithIcon(
                modifier = Modifier.padding(8.dp),
                icon = if ((artCollectible?.visitorsCount ?: 0) > 0) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                text = artCollectible?.visitorsCount?.toString()
                    ?: stringResource(id = R.string.no_text_value_small)
            )
        }
        Text(
            text = artCollectible?.metadata?.description ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = if(showPreviewDescription) {
                DEFAULT_LIMIT_DESC_MAX_LINES
            } else {
                Int.MAX_VALUE
            }
        )
    }
}