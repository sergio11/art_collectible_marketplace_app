package com.dreamsoftware.artcollectibles.ui.screens.visitors


import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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

data class VisitorsScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun VisitorsScreen(
    args: VisitorsScreenArgs,
    viewModel: VisitorsViewModel = hiltViewModel(),
    onGoToArtistDetail: (artist: UserInfo) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = VisitorsUiState(),
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
            load(args.tokenId)
        }
        VisitorsComponent(
            context = context,
            state = uiState,
            lazyGridState = lazyGridState,
            onGoToArtistDetail = onGoToArtistDetail
        )
    }
}

@Composable
internal fun VisitorsComponent(
    context: Context,
    state: VisitorsUiState,
    lazyGridState: LazyGridState,
    onGoToArtistDetail: (artist: UserInfo) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            isLoading = isLoading,
            items = userResult,
            appBarTitle = getTopAppBarTitle(userResult)
        ) { artist ->
            UserInfoArtistCard(
                modifier = Modifier
                    .height(262.dp)
                    .width(150.dp)
                    .clickable {
                        onGoToArtistDetail(artist)
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