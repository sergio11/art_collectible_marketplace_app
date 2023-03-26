package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

@Composable
fun ArtCollectiblesRow(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    context: Context,
    reverseStyle: Boolean = false,
    items: Iterable<ArtCollectible>,
    onShowAllItems: () -> Unit = {},
    onItemSelected: (item: ArtCollectible) -> Unit = {}
) {
    if (!Iterables.isEmpty(items)) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = titleRes),
                color = if (reverseStyle) {
                    Color.Black
                } else {
                    Color.White
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onShowAllItems() },
                fontFamily = montserratFontFamily,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleLarge
            )
            LazyRow(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(Iterables.size(items)) { idx ->
                    with(Iterables.get(items, idx)) {
                        ArtCollectibleMiniCard(
                            context = context,
                            reverseStyle = reverseStyle,
                            artCollectible = this
                        ) {
                            onItemSelected(this)
                        }
                    }
                }
            }
        }
    }
}