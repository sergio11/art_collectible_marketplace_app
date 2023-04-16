package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTopAppBar
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabsTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import java.math.BigInteger

@Composable
fun MyTokensScreen(
    navController: NavController,
    viewModel: MyTokensViewModel = hiltViewModel(),
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = MyTokensUiState(),
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
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadTokens()
        }
        MyTokensComponent(
            navController = navController,
            context = context,
            state = uiState,
            lazyGridState = lazyGridState,
            onNewTabSelected = ::onNewTabSelected,
            onTokenClicked = {
                onGoToTokenDetail(it.id)
            },
            onRetryCalled = { loadTokens() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyTokensComponent(
    navController: NavController,
    context: Context,
    state: MyTokensUiState,
    lazyGridState: LazyGridState,
    onNewTabSelected: (type: MyTokensTabsTypeEnum) -> Unit,
    onTokenClicked: (token: ArtCollectible) -> Unit,
    onRetryCalled: () -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            bottomBar = {
                BottomBar(navController)
            },
            topBar = {
                CommonTopAppBar(
                    titleRes = state.tabSelectedTitle ?: R.string.my_tokens_main_title,
                    centerTitle = true
                )
            },
            containerColor = Purple40
        ) { paddingValues ->
            ScreenBackgroundImage(imageRes = R.drawable.screen_background_2)
            Box(modifier = Modifier.padding(paddingValues)) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyTokensTabsRow(state, onNewTabSelected)
                    MyTokensLazyList(context, state, lazyGridState, onTokenClicked)
                    ErrorStateNotificationComponent(
                        isVisible = tokens.isEmpty() || !errorMessage.isNullOrBlank(),
                        imageRes = if (tokens.isEmpty()) {
                            R.drawable.not_data_found
                        } else {
                            R.drawable.error_occurred
                        },
                        title = errorMessage ?: stringResource(
                            id = if (tabSelectedType == MyTokensTabsTypeEnum.TOKENS_OWNED) {
                                R.string.my_tokens_tab_tokens_owned_not_found_error
                            } else {
                                R.string.my_tokens_tab_tokens_created_not_found_error
                            }
                        ),
                        isRetryButtonVisible = true,
                        onRetryCalled = onRetryCalled
                    )
                }
            }
        }
    }
}

@Composable
private fun MyTokensTabsRow(
    state: MyTokensUiState,
    onNewTabSelected: (type: MyTokensTabsTypeEnum) -> Unit
) {
    with(state) {
        if (tabs.isNotEmpty()) {
            TabRow(
                selectedTabIndex = tabSelectedIndex,
                containerColor = Color.White.copy(alpha = 0.9f)) {
                tabs.forEach { tab ->
                    Tab(
                        selected = tab.isSelected,
                        onClick = { onNewTabSelected(tab.type) },
                        icon = {
                            Image(
                                painter = painterResource(tab.iconRes),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyTokensLazyList(
    context: Context,
    state: MyTokensUiState,
    lazyGridState: LazyGridState,
    onTokenClicked: (token: ArtCollectible) -> Unit
) {
    with(state) {
        if (!isLoading) {
            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 8.dp).padding(top = 8.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                state = lazyGridState
            ) {
                items(tokens.size) { index ->
                    ArtCollectibleMiniCard(context = context, artCollectible = tokens[index]) {
                        onTokenClicked(tokens[index])
                    }
                }
            }
        }
    }
}