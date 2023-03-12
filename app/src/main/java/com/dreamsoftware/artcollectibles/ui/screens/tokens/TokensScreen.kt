package com.dreamsoftware.artcollectibles.ui.screens.tokens


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleMiniCard
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

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
    onGoToTokenDetail: (token: ArtCollectible) -> Unit
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
    val lazyGridState = rememberLazyGridState()
    val context = LocalContext.current
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            with(args) {
                if(viewType == TokensScreenArgs.ViewTypeEnum.OWNED) {
                    loadTokensOwnedBy(userAddress)
                } else {
                    loadTokensCreatedBy(userAddress)
                }
            }
        }
        TokensComponent(
            context = context,
            state = uiState,
            args = args,
            lazyGridState = lazyGridState,
            onGoToTokenDetail = onGoToTokenDetail
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TokensComponent(
    context: Context,
    state: TokensUiState,
    args: TokensScreenArgs,
    lazyGridState: LazyGridState,
    onGoToTokenDetail: (token: ArtCollectible) -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            getTopAppBarTitle(args, tokensResult),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontFamily = montserratFontFamily,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Purple40
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column {
                    LazyVerticalGrid(
                        modifier = Modifier.padding(8.dp),
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        state = lazyGridState,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(Iterables.size(tokensResult)) { index ->
                            val token = Iterables.get(tokensResult, index)
                            ArtCollectibleMiniCard(context = context, artCollectible = token) {
                                onGoToTokenDetail(token)
                            }
                        }

                    }
                }
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