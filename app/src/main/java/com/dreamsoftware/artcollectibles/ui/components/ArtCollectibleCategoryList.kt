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
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

@Composable
fun ArtCollectibleCategoryList(
    context: Context,
    @StringRes titleRes: Int,
    categories: Iterable<ArtCollectibleCategory>,
    onCategoryClicked: (artCollectibleCategory: ArtCollectibleCategory) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Text(
            text = stringResource(id = titleRes),
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow(
            modifier = Modifier.padding(vertical = 30.dp),
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
}