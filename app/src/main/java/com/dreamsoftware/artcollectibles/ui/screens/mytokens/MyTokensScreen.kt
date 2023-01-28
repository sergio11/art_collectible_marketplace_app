package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.ScreenBackgroundImage
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabsTypeEnum

@Composable
fun MyTokensScreen(
    navController: NavController,
    viewModel: MyTokensViewModel = hiltViewModel()
) {
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
    with(viewModel) {
        MyTokensComponent(
            navController = navController,
            state = uiState,
            onNewTabSelected = ::onNewTabSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyTokensComponent(
    navController: NavController,
    state: MyTokensUiState,
    onNewTabSelected: (type: MyTokensTabsTypeEnum) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage(imageRes = R.drawable.common_background)
            Column {
                MyTokensTabsRow(state, onNewTabSelected)
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
        if(tabs.isNotEmpty()) {
            TabRow(selectedTabIndex = tabs.indexOfFirst { it.isSelected }) {
                tabs.forEach { tab ->
                    Tab(
                        selected = tab.isSelected,
                        onClick = { onNewTabSelected(tab.type) },
                        text = {
                            Text(
                                text = stringResource(id = tab.titleRes),
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