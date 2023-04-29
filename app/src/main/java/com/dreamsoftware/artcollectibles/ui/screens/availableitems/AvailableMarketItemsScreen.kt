package com.dreamsoftware.artcollectibles.ui.screens.availableitems


import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleForSaleCard
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalGridScreen
import com.google.common.collect.Iterables
import java.math.BigInteger


@Composable
fun AvailableMarketItemsScreen(
    viewModel: AvailableMarketItemsViewModel = hiltViewModel(),
    onMarketItemSelected: (tokenId: BigInteger) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = AvailableMarketItemsUiState(),
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
    val lazyGridState = rememberLazyGridState()
    val context = LocalContext.current
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        AvailableMarketItemsComponent(
            context = context,
            snackBarHostState = snackBarHostState,
            state = uiState,
            lazyGridState = lazyGridState,
            onRetryCalled = ::load,
            onMarketItemSelected = onMarketItemSelected
        )
    }
}

@Composable
internal fun AvailableMarketItemsComponent(
    context: Context,
    snackBarHostState: SnackbarHostState,
    state: AvailableMarketItemsUiState,
    lazyGridState: LazyGridState,
    onRetryCalled: () -> Unit,
    onMarketItemSelected: (tokenId: BigInteger) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = availableItems,
            onRetryCalled = onRetryCalled,
            noDataFoundMessageId = R.string.available_market_no_items_found_title,
            appBarTitle = getTopAppBarTitle(availableItems)
        ) { availableItem ->
            ArtCollectibleForSaleCard(
                context = context,
                artCollectibleForSale = availableItem,
                onClicked = {
                    onMarketItemSelected(availableItem.token.id)
                }
            )
        }
    }
}

@Composable
private fun getTopAppBarTitle(
    items: Iterable<ArtCollectibleForSale>
) = if(Iterables.isEmpty(items)) {
    stringResource(id = R.string.available_market_items_title_default)
} else {
    stringResource(id = R.string.available_market_items_title_count, Iterables.size(items))
}