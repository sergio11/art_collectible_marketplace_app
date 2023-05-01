package com.dreamsoftware.artcollectibles.ui.screens.tokens


import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleMiniCard
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalGridScreen
import com.google.common.collect.Iterables
import java.math.BigInteger

data class TokensScreenArgs(
    val userAddress: String,
    val viewType: ViewTypeEnum
) {
    enum class ViewTypeEnum {
        OWNED, CREATED
    }
}

@Composable
fun TokensScreen(
    args: TokensScreenArgs,
    viewModel: TokensViewModel = hiltViewModel(),
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = TokensUiState(),
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
    val context = LocalContext.current
    with(viewModel) {
        with(args) {
            LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
                if(viewType == TokensScreenArgs.ViewTypeEnum.OWNED) {
                    loadTokensOwnedBy(userAddress)
                } else {
                    loadTokensCreatedBy(userAddress)
                }
            }
            TokensComponent(
                context = context,
                snackBarHostState = snackBarHostState,
                state = uiState,
                args = args,
                lazyGridState = lazyGridState,
                noDataFoundMessageId = if(viewType == TokensScreenArgs.ViewTypeEnum.OWNED) {
                    R.string.profile_tokens_owned_by_user_not_found_message
                } else {
                    R.string.profile_tokens_created_by_user_not_found_message
                },
                onRetryCalled = {
                    if(viewType == TokensScreenArgs.ViewTypeEnum.OWNED) {
                        loadTokensOwnedBy(userAddress)
                    } else {
                        loadTokensCreatedBy(userAddress)
                    }
                },
                onGoToTokenDetail = onGoToTokenDetail,
                onBackPressed = onBackPressed
            )
        }
    }
}

@Composable
internal fun TokensComponent(
    context: Context,
    snackBarHostState: SnackbarHostState,
    state: TokensUiState,
    args: TokensScreenArgs,
    lazyGridState: LazyGridState,
    @StringRes noDataFoundMessageId: Int,
    onRetryCalled: () -> Unit,
    onBackPressed: () -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = tokensResult,
            noDataFoundMessageId = noDataFoundMessageId,
            onRetryCalled = onRetryCalled,
            onBackPressed = onBackPressed,
            appBarTitle = getTopAppBarTitle(args, tokensResult)
        ) {token ->
            ArtCollectibleMiniCard(context = context, artCollectible = token) {
                onGoToTokenDetail(token.id)
            }
        }
    }
}

@Composable
private fun getTopAppBarTitle(
    args: TokensScreenArgs,
    tokensResult: Iterable<ArtCollectible>
) = if(args.viewType == TokensScreenArgs.ViewTypeEnum.OWNED) {
    if(Iterables.isEmpty(tokensResult)) {
        stringResource(id = R.string.profile_tokens_owned_by_user_title_default)
    } else {
        stringResource(id = R.string.profile_tokens_owned_by_user_title_count, Iterables.size(tokensResult))
    }
} else {
    if(Iterables.isEmpty(tokensResult)) {
        stringResource(id = R.string.profile_tokens_created_by_user_title_default)
    } else {
        stringResource(id = R.string.profile_tokens_created_by_user_title_count, Iterables.size(tokensResult))
    }
}