package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.google.common.collect.Iterables

@Composable
fun <T: Any>CommonVerticalGridScreen(
    lazyGridState: LazyGridState,
    snackBarHostState: SnackbarHostState,
    isLoading: Boolean,
    items: Iterable<T>,
    appBarTitle: String,
    @StringRes noDataFoundMessageId: Int,
    onRetryCalled: () -> Unit,
    onBuildContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    LoadingDialog(isShowingDialog = isLoading)
    BasicScreen(
        snackBarHostState = snackBarHostState,
        titleText = appBarTitle,
        centerTitle = true,
        backgroundContent = {
            ErrorStateNotificationComponent(
                modifier = Modifier.align(Alignment.Center),
                isVisible = !isLoading && Iterables.isEmpty(items),
                imageRes = R.drawable.not_data_found,
                title = stringResource(id = noDataFoundMessageId),
                isRetryButtonVisible = true,
                onRetryCalled = onRetryCalled
            )
        },
        screenContent = {
            LazyVerticalGrid(
                modifier = Modifier.padding(8.dp),
                columns = GridCells.Adaptive(minSize = 150.dp),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(Iterables.size(items)) { index ->
                    onBuildContent(item = Iterables.get(items, index))
                }
            }
        }
    )
}