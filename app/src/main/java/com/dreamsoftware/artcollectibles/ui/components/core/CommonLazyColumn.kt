package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.common.collect.Iterables

@Composable
fun <T: Any>CommonLazyColumn(
    state: LazyListState,
    items: Iterable<T>,
    onBuildContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        state = state,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(Iterables.size(items)) { index ->
            onBuildContent(item = Iterables.get(items, index))
        }
    }
}