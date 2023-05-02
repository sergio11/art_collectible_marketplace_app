package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.core.CollectionRow

@Composable
fun ArtCollectiblesRow(
    @StringRes titleRes: Int,
    context: Context,
    reverseStyle: Boolean = false,
    items: Iterable<ArtCollectible>,
    onShowAllItems: (() -> Unit)? = null,
    onItemSelected: (item: ArtCollectible) -> Unit = {}
) {
    CollectionRow(
        modifier = Modifier
            .padding(vertical = 10.dp),
        titleRes = titleRes,
        reverseStyle = reverseStyle,
        items = items,
        onBuildItem = { modifier, item ->
            ArtCollectibleMiniCard(
                context = context,
                modifier = modifier,
                reverseStyle = reverseStyle,
                artCollectible = item
            )
        },
        onShowAllItems = onShowAllItems,
        onItemSelected = onItemSelected
    )
}