package com.dreamsoftware.artcollectibles.ui.screens.tokenhistory


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.TokenTransactionItem
import com.dreamsoftware.artcollectibles.ui.components.UserAccountProfilePicture
import com.dreamsoftware.artcollectibles.ui.extensions.format
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables
import java.math.BigInteger

data class TokenHistoryScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenHistoryScreen(
    args: TokenHistoryScreenArgs,
    viewModel: TokenHistoryViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = TokenHistoryUiState(),
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
            load(args.tokenId)
        }
        TokenHistoryComponent(
            state = uiState,
            lazyListState = lazyListState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TokenHistoryComponent(
    state: TokenHistoryUiState,
    lazyListState: LazyListState
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            getTopAppBarTitle(tokenHistory),
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
                    LazyColumn(
                        modifier = Modifier.padding(8.dp),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(Iterables.size(tokenHistory)) { index ->
                            TokenTransactionItem(item = Iterables.get(tokenHistory, index))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getTopAppBarTitle(
    data: Iterable<ArtCollectibleForSale>
) = if (Iterables.isEmpty(data)) {
    stringResource(id = R.string.token_history_title_default)
} else {
    stringResource(id = R.string.token_history_title_count, Iterables.size(data))
}