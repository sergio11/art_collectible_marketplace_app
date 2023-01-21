package com.dreamsoftware.artcollectibles.ui.screens.search


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.SearchView
import com.google.common.collect.Iterables
import com.dreamsoftware.artcollectibles.ui.components.UserInfoArtistCard
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = SearchUiState(),
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
    LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
        viewModel.load()
    }
    SearchComponent(
        context = context,
        state = uiState,
        lazyGridState = lazyGridState,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchComponent(
    context: Context,
    state: SearchUiState,
    lazyGridState: LazyGridState,
    navController: NavController
) {
    LoadingDialog(isShowingDialog = state.isLoading)
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        val searchViewState = remember { mutableStateOf(TextFieldValue("")) }
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchView(searchViewState)
            LazyVerticalGrid(
                modifier = Modifier.padding(8.dp),
                columns = GridCells.Adaptive(minSize = 150.dp),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                with(state) {
                    items(Iterables.size(userResult)) { index ->
                        UserInfoArtistCard(
                            modifier = Modifier
                                .height(262.dp)
                                .width(150.dp),
                            context = context,
                            user = Iterables.get(userResult, index)
                        )
                    }
                }
            }
        }
    }
}