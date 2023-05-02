package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.ui.components.core.CollectionRow

@Composable
fun ArtCollectibleCategoryList(
    context: Context,
    @StringRes titleRes: Int,
    categories: Iterable<ArtCollectibleCategory>,
    onCategoryClicked: (artCollectibleCategory: ArtCollectibleCategory) -> Unit) {
    CollectionRow(
        modifier = Modifier
            .padding(vertical = 10.dp),
        titleRes = titleRes,
        items = categories,
        onBuildItem = { modifier, item ->
            ArtCollectibleCategoryCard(
                modifier = modifier,
                context = context,
                title = item.name,
                imageUrl = item.imageUrl
            )
        },
        onItemSelected = onCategoryClicked
    )
}