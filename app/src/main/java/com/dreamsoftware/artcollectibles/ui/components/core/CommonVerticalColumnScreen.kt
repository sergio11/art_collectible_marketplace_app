package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog

@Composable
fun <T: Any>CommonVerticalColumnScreen(
    lazyListState: LazyListState,
    snackBarHostState: SnackbarHostState,
    isLoading: Boolean,
    items: Iterable<T>,
    appBarTitle: String,
    onBackPressed: () -> Unit,
    onBuildContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    LoadingDialog(isShowingDialog = isLoading)
    BasicScreen(
        snackBarHostState = snackBarHostState,
        titleText = appBarTitle,
        centerTitle = true,
        navigationAction = TopBarAction(
            iconRes = R.drawable.back_icon,
            onActionClicked = onBackPressed
        ),
        screenContent = {
            CommonLazyColumn(
                state = lazyListState,
                items = items,
                onBuildContent = onBuildContent
            )
        }
    )
}