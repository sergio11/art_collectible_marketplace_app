package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.google.common.collect.Iterables

@Composable
fun <T: Any>CommonVerticalColumnScreen(
    lazyListState: LazyListState,
    snackBarHostState: SnackbarHostState,
    isLoading: Boolean,
    items: Iterable<T>,
    appBarTitle: String,
    onBuildContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    LoadingDialog(isShowingDialog = isLoading)
    BasicScreen(
        snackBarHostState = snackBarHostState,
        titleText = appBarTitle,
        centerTitle = true,
        screenContent = {
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(Iterables.size(items)) { index ->
                    onBuildContent(item = Iterables.get(items, index))
                }
            }
        }
    )
}