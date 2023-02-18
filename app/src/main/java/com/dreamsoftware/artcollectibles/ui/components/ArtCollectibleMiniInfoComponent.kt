package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun ArtCollectibleMiniInfoComponent(
    modifier: Modifier = Modifier,
    artCollectible: ArtCollectible?
) {
    Column(modifier = modifier) {
        Text(
            text = artCollectible?.name ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row {
            TokenCreatorInfoComponent(
                modifier = Modifier.padding(8.dp),
                artCollectible?.author
            )
            TokenRoyaltyComponent(
                modifier = Modifier.padding(8.dp),
                artCollectible?.royalty
            )
            FavoriteCountComponent(
                modifier = Modifier.padding(8.dp),
                artCollectible = artCollectible,
                defaultColor = Purple500
            )
        }
        Text(
            text = artCollectible?.description ?: stringResource(id = R.string.no_text_value),
            fontFamily = montserratFontFamily,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}