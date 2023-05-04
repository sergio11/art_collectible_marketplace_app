package com.dreamsoftware.artcollectibles.ui.screens.discovery


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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleCategoryCard
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.UserInfoArtistCard
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.CommonLazyVerticalGrid
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTabsRow
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTitle
import com.dreamsoftware.artcollectibles.ui.model.DiscoveryTabsTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun DiscoveryScreen(
    navController: NavController,
    viewModel: DiscoveryViewModel = hiltViewModel(),
    onGoToArtistDetail: (userUid: String) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = DiscoveryUiState(),
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
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        DiscoveryComponent(
            snackBarHostState = snackBarHostState,
            context = context,
            state = uiState,
            lazyGridState = lazyGridState,
            navController = navController,
            onTermChanged = ::onTermChanged,
            onResetSearch = ::onResetSearch,
            onNewTabSelected = ::onNewTabSelected,
            onGoToArtistDetail = onGoToArtistDetail
        )
    }
}

@Composable
internal fun DiscoveryComponent(
    snackBarHostState: SnackbarHostState,
    context: Context,
    state: DiscoveryUiState,
    lazyGridState: LazyGridState,
    navController: NavController,
    onTermChanged: (String) -> Unit,
    onResetSearch: () -> Unit,
    onNewTabSelected: (type: DiscoveryTabsTypeEnum) -> Unit,
    onGoToArtistDetail: (userUid: String) -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        BasicScreen(
            titleRes = tabs.tabSelectedTitle() ?: R.string.discovery_main_title_default_text,
            snackBarHostState = snackBarHostState,
            centerTitle = true,
            navController = navController,
            hasBottomBar = true,
            screenContainerColor = Purple40,
            screenContent = {
                CommonTabsRow(tabs, onNewTabSelected)
                if (!isLoading) {
                    CommonLazyVerticalGrid(state = lazyGridState, items = items) {
                        if(it is UserInfo) {
                            UserInfoArtistCard(
                                modifier = Modifier
                                    .height(262.dp)
                                    .width(150.dp)
                                    .clickable {
                                        onGoToArtistDetail(it.uid)
                                    },
                                context = context,
                                user = it
                            )
                        } else if (it is ArtCollectibleCategory) {
                            ArtCollectibleCategoryCard(
                                context = context,
                                title = it.name,
                                imageUrl = it.imageUrl
                            )
                        }
                    }
                }
                /*SearchView(
                    context = context,
                    term = state.searchTerm,
                    onTermChanged = onTermChanged,
                    onClearClicked = onResetSearch
                )*/
            }
        )
    }
}