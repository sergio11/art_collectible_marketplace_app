package com.dreamsoftware.artcollectibles.ui.screens.tokenhistory

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.TokenTransactionItem
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalColumnScreen
import com.google.common.collect.Iterables
import java.math.BigInteger

data class TokenHistoryScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenHistoryScreen(
    args: TokenHistoryScreenArgs,
    viewModel: TokenHistoryViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = TokenHistoryUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load(args.tokenId)
        }
        TokenHistoryComponent(
            state = uiState,
            snackBarHostState = snackBarHostState,
            lazyListState = lazyListState,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
internal fun TokenHistoryComponent(
    state: TokenHistoryUiState,
    snackBarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    onBackPressed: () -> Unit
) {
    with(state) {
        CommonVerticalColumnScreen(
            lazyListState = lazyListState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = tokenHistory,
            onBackPressed = onBackPressed,
            appBarTitle = getTopAppBarTitle(tokenHistory)
        ) { item ->
            TokenTransactionItem(item = item)
        }
    }
}

@Composable
private fun getTopAppBarTitle(
    data: Iterable<ArtCollectibleForSale>
) = if (Iterables.isEmpty(data)) {
    stringResource(id = R.string.token_history_title_default)
} else {
    stringResource(id = R.string.token_history_title_count, Iterables.size(data))
}