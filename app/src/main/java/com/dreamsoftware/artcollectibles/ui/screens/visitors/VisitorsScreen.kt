package com.dreamsoftware.artcollectibles.ui.screens.visitors


import android.content.Context
import androidx.compose.foundation.clickable
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
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.UserInfoArtistCard
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

data class VisitorsScreenArgs(
    val tokenId: String
)

@Composable
fun VisitorsScreen(
    args: VisitorsScreenArgs,
    viewModel: VisitorsViewModel = hiltViewModel(),
    onGoToArtistDetail: (artist: UserInfo) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = VisitorsUiState(),
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
            load(args.tokenId)
        }
        VisitorsComponent(
            context = context,
            state = uiState,
            lazyGridState = lazyGridState,
            onGoToArtistDetail = onGoToArtistDetail
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VisitorsComponent(
    context: Context,
    state: VisitorsUiState,
    lazyGridState: LazyGridState,
    onGoToArtistDetail: (artist: UserInfo) -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            getTopAppBarTitle(userResult),
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
                        items(Iterables.size(userResult)) { index ->
                            val artist = Iterables.get(userResult, index)
                            UserInfoArtistCard(
                                modifier = Modifier
                                    .height(262.dp)
                                    .width(150.dp)
                                    .clickable {
                                        onGoToArtistDetail(artist)
                                    },
                                context = context,
                                user = artist
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getTopAppBarTitle(data: Iterable<UserInfo>) = if(Iterables.isEmpty(data)) {
    stringResource(id = R.string.visitors_detail_title_default)
} else {
    stringResource(id = R.string.visitors_detail_title_count, Iterables.size(data))
}