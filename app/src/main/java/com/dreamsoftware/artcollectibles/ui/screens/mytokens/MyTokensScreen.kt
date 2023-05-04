package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleMiniCard
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTabsRow
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
    val snackBarHostState = remember { SnackbarHostState() }
    val lazyGridState = rememberLazyGridState()
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadTokens()
        }
        MyTokensComponent(
            navController = navController,
            snackBarHostState = snackBarHostState,
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

@Composable
internal fun MyTokensComponent(
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    context: Context,
    state: MyTokensUiState,
    lazyGridState: LazyGridState,
    onNewTabSelected: (type: MyTokensTabsTypeEnum) -> Unit,
    onTokenClicked: (token: ArtCollectible) -> Unit,
    onRetryCalled: () -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        BasicScreen(
            snackBarHostState = snackBarHostState,
            titleRes = tabSelectedTitle ?: R.string.my_tokens_main_title,
            centerTitle = true,
            navController = navController,
            hasBottomBar = true,
            screenContainerColor = Purple40,
            screenContent = {
                CommonTabsRow(tabs, tabSelectedIndex, onNewTabSelected)
                MyTokensLazyList(context, state, lazyGridState, onTokenClicked)
                ErrorStateNotificationComponent(
                    isVisible = !isLoading && tokens.isEmpty() || !errorMessage.isNullOrBlank(),
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
            })
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
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
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