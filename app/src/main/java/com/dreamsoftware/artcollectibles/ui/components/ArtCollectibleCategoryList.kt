package com.dreamsoftware.artcollectibles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.categories
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun ArtCollectibleCategoryList() {
    LazyRow(
        modifier = Modifier.padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { category ->
            ArtCollectibleCategoryCard(title = category.title, image = painterResource(id = category.image))
        }
    }
}

@Preview
@Composable
fun PreviewCategoryList() {
    ArtCollectibleMarketplaceTheme {
        ArtCollectibleCategoryList()
    }
}