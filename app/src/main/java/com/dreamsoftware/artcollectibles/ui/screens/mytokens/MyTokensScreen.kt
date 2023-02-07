package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleCard
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.ScreenBackgroundImage
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabsTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@Composable
fun MyTokensScreen(
    navController: NavController,
    viewModel: MyTokensViewModel = hiltViewModel(),
    onGoToTokenDetail: (token: ArtCollectible) -> Unit
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
    val lazyListState = rememberLazyListState()
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadTokens()
        }
        MyTokensComponent(
            navController = navController,
            context = context,
            state = uiState,
            lazyListState = lazyListState,
            onNewTabSelected = ::onNewTabSelected,
            onTokenClicked = onGoToTokenDetail,
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
    lazyListState: LazyListState,
    onNewTabSelected: (type: MyTokensTabsTypeEnum) -> Unit,
    onTokenClicked: (token: ArtCollectible) -> Unit,
    onRetryCalled: () -> Unit
) {
    LoadingDialog(isShowingDialog = state.isLoading)
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage(imageRes = R.drawable.common_background)
            Column {
                MyTokensTabsRow(state, onNewTabSelected)
                MyTokensLazyList(context, state, lazyListState, onTokenClicked, onRetryCalled)
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
            TabRow(selectedTabIndex = tabSelectedIndex) {
                tabs.forEach { tab ->
                    Tab(
                        selected = tab.isSelected,
                        onClick = { onNewTabSelected(tab.type) },
                        text = {
                            Text(
                                text = stringResource(id = tab.titleRes),
                                fontFamily = montserratFontFamily,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
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
    lazyListState: LazyListState,
    onTokenClicked: (token: ArtCollectible) -> Unit,
    onRetryCalled: () -> Unit
) {
    with(state) {
        if(!isLoading) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tokens.size) { index ->
                    ArtCollectibleCard(
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .clickable { onTokenClicked(tokens[index]) },
                        context = context,
                        artCollectible = tokens[index]
                    )
                }
            }
            ErrorStateNotificationComponent(
                isVisible = tokens.isEmpty(),
                imageRes = R.drawable.not_data_found,
                titleRes = if(tabSelectedType == MyTokensTabsTypeEnum.TOKENS_OWNED) {
                    R.string.my_tokens_tab_tokens_owned_not_found_error
                } else {
                    R.string.my_tokens_tab_tokens_created_not_found_error
                },
                isRetryButtonVisible = true,
                onRetryCalled = onRetryCalled
            )
        }
    }
}