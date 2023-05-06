package com.dreamsoftware.artcollectibles.ui.screens.discovery


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
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTitle
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTypeOrDefault
import com.dreamsoftware.artcollectibles.ui.model.DiscoveryTabsTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.google.common.collect.Iterables

@Composable
fun DiscoveryScreen(
    navController: NavController,
    viewModel: DiscoveryViewModel = hiltViewModel(),
    onGoToArtistDetail: (userUid: String) -> Unit,
    onGoToCategoryDetail: (categoryUid: String) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceUiState(
        initialState = DiscoveryUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
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
            onSearchingModeChanged = ::onSearchingModeChanged,
            onRetryCalled = ::load,
            onTermChanged = ::onTermChanged,
            onResetSearch = ::onResetSearch,
            onNewTabSelected = ::onNewTabSelected,
            onGoToArtistDetail = onGoToArtistDetail,
            onGoToCategoryDetail = onGoToCategoryDetail
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
    onSearchingModeChanged: (enabled: Boolean) -> Unit,
    onRetryCalled: () -> Unit,
    onTermChanged: (String) -> Unit,
    onResetSearch: () -> Unit,
    onNewTabSelected: (type: DiscoveryTabsTypeEnum) -> Unit,
    onGoToArtistDetail: (userUid: String) -> Unit,
    onGoToCategoryDetail: (categoryUid: String) -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        BasicScreen(
            titleRes = tabs.tabSelectedTitle() ?: R.string.discovery_main_title_default_text,
            snackBarHostState = snackBarHostState,
            centerTitle = true,
            navController = navController,
            hasBottomBar = true,
            menuActions = if (tabs.tabSelectedTypeOrDefault(default = DiscoveryTabsTypeEnum.DIGITAL_ARTISTS) == DiscoveryTabsTypeEnum.DIGITAL_ARTISTS) {
                listOf(
                    TopBarAction(
                        iconRes = R.drawable.search_icon,
                        onActionClicked = {
                            onSearchingModeChanged(true)
                        }
                    )
                )
            } else {
                emptyList()
            },
            screenContainerColor = Purple40,
            onBuildCustomTopBar = if (isSearchingModeEnabled) {
                {
                    SearchView(
                        context = context,
                        term = searchTerm,
                        onCloseClicked = {
                            onResetSearch()
                            onSearchingModeChanged(false)
                        },
                        onTermChanged = onTermChanged,
                        onClearClicked = onResetSearch
                    )
                }
            } else {
                null
            },
            screenContent = {
                CommonTabsRow(tabs, onNewTabSelected)
                if (!isLoading) {
                    CommonLazyVerticalGrid(state = lazyGridState, items = items) {
                        if (it is UserInfo) {
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
                                modifier = Modifier.clickable {
                                    onGoToCategoryDetail(it.uid)
                                },
                                context = context,
                                title = it.name,
                                imageUrl = it.imageUrl
                            )
                        }
                    }
                }
                ErrorStateNotificationComponent(
                    isVisible = !isLoading && Iterables.isEmpty(items) || !errorMessage.isNullOrBlank(),
                    imageRes = if (errorMessage.isNullOrBlank()) {
                        R.drawable.not_data_found
                    } else {
                        R.drawable.error_occurred
                    },
                    title = errorMessage ?: stringResource(
                        id = if (tabs.tabSelectedTypeOrDefault(default = DiscoveryTabsTypeEnum.DIGITAL_ARTISTS) == DiscoveryTabsTypeEnum.DIGITAL_ARTISTS) {
                            R.string.discovery_tab_digital_artists_not_found_error
                        } else {
                            R.string.discovery_tab_nft_categories_not_found_error
                        }
                    ),
                    isRetryButtonVisible = true,
                    onRetryCalled = onRetryCalled
                )
            }
        )
    }
}