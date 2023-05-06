package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleMiniCard
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.CommonLazyVerticalGrid
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTabsRow
import com.dreamsoftware.artcollectibles.ui.components.core.produceUiState
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTitle
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTypeOrDefault
import com.dreamsoftware.artcollectibles.ui.model.MyTokensTabsTypeEnum
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
    val uiState by produceUiState(
        initialState = MyTokensUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
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
            titleRes = tabs.tabSelectedTitle() ?: R.string.my_tokens_main_title,
            centerTitle = true,
            navController = navController,
            hasBottomBar = true,
            screenContainerColor = Purple40,
            screenContent = {
                CommonTabsRow(tabs, onNewTabSelected)
                if (!isLoading) {
                    CommonLazyVerticalGrid(state = lazyGridState, tokens) {
                        ArtCollectibleMiniCard(context = context, artCollectible = it) {
                            onTokenClicked(it)
                        }
                    }
                }
                ErrorStateNotificationComponent(
                    isVisible = !isLoading && tokens.isEmpty() || !errorMessage.isNullOrBlank(),
                    imageRes = if (errorMessage.isNullOrBlank()) {
                        R.drawable.not_data_found
                    } else {
                        R.drawable.error_occurred
                    },
                    title = errorMessage ?: stringResource(
                        id = if (tabs.tabSelectedTypeOrDefault(default = MyTokensTabsTypeEnum.TOKENS_OWNED) == MyTokensTabsTypeEnum.TOKENS_OWNED) {
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