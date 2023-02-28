package com.dreamsoftware.artcollectibles.ui.screens.followers


import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.UserInfoArtistCard
import com.google.common.collect.Iterables

data class FollowersScreenArgs(
    val userUid: String,
    val viewType: ViewTypeEnum
) {
    enum class ViewTypeEnum {
        FOLLOWERS, FOLLOWING
    }
}


@Composable
fun FollowersScreen(
    navController: NavController,
    args: FollowersScreenArgs,
    viewModel: FollowersViewModel = hiltViewModel(),
    onGoToArtistDetail: (artist: UserInfo) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = FollowersUiState(),
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
        FollowersComponent(
            context = context,
            state = uiState,
            lazyGridState = lazyGridState,
            navController = navController,
            onGoToArtistDetail = onGoToArtistDetail
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FollowersComponent(
    context: Context,
    state: FollowersUiState,
    lazyGridState: LazyGridState,
    navController: NavController,
    onGoToArtistDetail: (artist: UserInfo) -> Unit
) {
    LoadingDialog(isShowingDialog = state.isLoading)
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                LazyVerticalGrid(
                    modifier = Modifier.padding(8.dp),
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = lazyGridState,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    with(state) {
                        items(Iterables.size(userResult)) { index ->
                            val artist = Iterables.get(userResult, index)
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
            }
        }
    }
}