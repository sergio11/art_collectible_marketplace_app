package com.dreamsoftware.artcollectibles.ui.screens.tokenhistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.TokenTransactionItem
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalColumnScreen
import com.dreamsoftware.artcollectibles.ui.components.core.produceUiState
import com.google.common.collect.Iterables
import java.math.BigInteger

data class TokenHistoryScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenHistoryScreen(
    args: TokenHistoryScreenArgs,
    viewModel: TokenHistoryViewModel = hiltViewModel(),
    onGoToMarketItemHistoryDetail: (marketItemId: BigInteger) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceUiState(
        initialState = TokenHistoryUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
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
            onGoToMarketItemHistoryDetail = onGoToMarketItemHistoryDetail,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
internal fun TokenHistoryComponent(
    state: TokenHistoryUiState,
    snackBarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    onGoToMarketItemHistoryDetail: (marketItemId: BigInteger) -> Unit,
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
            TokenTransactionItem(
                modifier = Modifier.clickable { onGoToMarketItemHistoryDetail(item.marketItemId) },
                item = item
            )
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