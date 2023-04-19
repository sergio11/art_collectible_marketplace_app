package com.dreamsoftware.artcollectibles.ui.screens.followers


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
    args: FollowersScreenArgs,
    viewModel: FollowersViewModel = hiltViewModel(),
    onGoToArtistDetail: (userUid: String) -> Unit
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
            with(args) {
                if(viewType == FollowersScreenArgs.ViewTypeEnum.FOLLOWING) {
                    loadFollowing(userUid)
                } else {
                    loadFollowers(userUid)
                }
            }
        }
        FollowersComponent(
            context = context,
            state = uiState,
            args = args,
            lazyGridState = lazyGridState,
            onGoToArtistDetail = onGoToArtistDetail
        )
    }
}

@Composable
internal fun FollowersComponent(
    context: Context,
    state: FollowersUiState,
    args: FollowersScreenArgs,
    lazyGridState: LazyGridState,
    onGoToArtistDetail: (userUid: String) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            isLoading = isLoading,
            items = userResult,
            appBarTitle = getTopAppBarTitle(args, userResult)
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
private fun getTopAppBarTitle(
    args: FollowersScreenArgs,
    userResult: Iterable<UserInfo>
) = if(args.viewType == FollowersScreenArgs.ViewTypeEnum.FOLLOWING) {
    if(Iterables.isEmpty(userResult)) {
        stringResource(id = R.string.following_detail_title_default)
    } else {
        stringResource(id = R.string.following_detail_title_count, Iterables.size(userResult))
    }
} else {
    if(Iterables.isEmpty(userResult)) {
        stringResource(id = R.string.followers_detail_title_default)
    } else {
        stringResource(id = R.string.followers_detail_title_count, Iterables.size(userResult))
    }
}