package com.dreamsoftware.artcollectibles.ui.screens.visitors


import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.UserInfoArtistCard
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalGridScreen
import com.dreamsoftware.artcollectibles.ui.components.core.produceUiState
import com.google.common.collect.Iterables
import java.math.BigInteger

data class VisitorsScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun VisitorsScreen(
    args: VisitorsScreenArgs,
    viewModel: VisitorsViewModel = hiltViewModel(),
    onGoToArtistDetail: (userUid: String) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceUiState(
        initialState = VisitorsUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
    val snackBarHostState = remember { SnackbarHostState() }
    val lazyGridState = rememberLazyGridState()
    val context = LocalContext.current
    with(viewModel) {
        with(args) {
            LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
                load(tokenId)
            }
            VisitorsComponent(
                context = context,
                snackBarHostState = snackBarHostState,
                state = uiState,
                lazyGridState = lazyGridState,
                onRetryCalled = { load(tokenId) },
                onBackPressed = onBackPressed,
                onGoToArtistDetail = onGoToArtistDetail
            )
        }
    }
}

@Composable
internal fun VisitorsComponent(
    context: Context,
    state: VisitorsUiState,
    snackBarHostState: SnackbarHostState,
    lazyGridState: LazyGridState,
    onRetryCalled: () -> Unit,
    onBackPressed: () -> Unit,
    onGoToArtistDetail: (userUid: String) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = userResult,
            onBackPressed = onBackPressed,
            onRetryCalled = onRetryCalled,
            noDataFoundMessageId = R.string.visitors_detail_not_found_message,
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
    stringResource(id = R.string.visitors_detail_title_default)
} else {
    stringResource(id = R.string.visitors_detail_title_count, Iterables.size(data))
}