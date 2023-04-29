package com.dreamsoftware.artcollectibles.ui.screens.favorites


import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.UserInfoArtistCard
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalGridScreen
import com.google.common.collect.Iterables
import java.math.BigInteger

data class FavoritesScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun FavoritesScreen(
    args: FavoritesScreenArgs,
    viewModel: FavoritesViewModel = hiltViewModel(),
    onGoToArtistDetail: (userUid: String) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = FavoritesUiState(),
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
            load(args.tokenId)
        }
        FavoritesComponent(
            context = context,
            snackBarHostState = snackBarHostState,
            state = uiState,
            lazyGridState = lazyGridState,
            onRetryCalled = { load(args.tokenId) },
            onGoToArtistDetail = onGoToArtistDetail
        )
    }
}

@Composable
internal fun FavoritesComponent(
    context: Context,
    snackBarHostState: SnackbarHostState,
    state: FavoritesUiState,
    lazyGridState: LazyGridState,
    onRetryCalled: () -> Unit,
    onGoToArtistDetail: (userUid: String) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = userResult,
            onRetryCalled = onRetryCalled,
            noDataFoundMessageId = R.string.favorites_detail_not_found_message,
            appBarTitle = getTopAppBarTitle(userResult)
        ) { artist ->
            UserInfoArtistCard(
                modifier = Modifier
                    .height(262.dp)
                    .width(150.dp)
                    .clickable {
                        onGoToArtistDetail(artist.uid)
                    },
                context = context,
                user = artist
            )
        }
    }
}

@Composable
private fun getTopAppBarTitle(data: Iterable<UserInfo>) = if(Iterables.isEmpty(data)) {
    stringResource(id = R.string.favorites_detail_title_default)
} else {
    stringResource(id = R.string.favorites_detail_title_count, Iterables.size(data))
}