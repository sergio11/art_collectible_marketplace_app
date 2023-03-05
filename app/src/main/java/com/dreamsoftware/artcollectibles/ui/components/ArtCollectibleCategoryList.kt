package com.dreamsoftware.artcollectibles.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.google.common.collect.Iterables

@Composable
fun ArtCollectibleCategoryList(
    context: Context,
    categories: Iterable<ArtCollectibleCategory>,
    onCategoryClicked: (artCollectibleCategory: ArtCollectibleCategory) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(Iterables.size(categories)) { category ->
            with(Iterables.get(categories, category)) {
                ArtCollectibleCategoryCard(modifier = Modifier.clickable {
                    onCategoryClicked(this)
                }, context = context, title = name, imageUrl = imageUrl)
            }
        }
    }
}