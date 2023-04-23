package com.dreamsoftware.artcollectibles.ui.screens.availableitems


import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
    val lazyGridState = rememberLazyGridState()
    val context = LocalContext.current
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        AvailableMarketItemsComponent(
            context = context,
            state = uiState,
            lazyGridState = lazyGridState,
            onMarketItemSelected = onMarketItemSelected
        )
    }
}

@Composable
internal fun AvailableMarketItemsComponent(
    context: Context,
    state: AvailableMarketItemsUiState,
    lazyGridState: LazyGridState,
    onMarketItemSelected: (tokenId: BigInteger) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            isLoading = isLoading,
            items = availableItems,
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