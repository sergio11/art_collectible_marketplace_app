package com.dreamsoftware.artcollectibles.ui.screens.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleForSaleCard
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = HomeUiState(),
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
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadData()
        }
        HomeComponent(
            navController = navController,
            context = context,
            uiState = uiState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeComponent(
    navController: NavController,
    context: Context,
    uiState: HomeUiState
) {
    with(uiState) {
        LoadingDialog(isShowingDialog = isLoading)
        Scaffold(
            bottomBar = {
                BottomBar(navController)
            },
            topBar = {
                TopAppBar(title = {
                    Text(
                        "Art Collectible Marketplace",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserratFontFamily,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent))
            },
            containerColor = Purple40
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                MarketplaceRow(context, "Your items for sale", sellingMarketItems)
                MarketplaceRow(context, "Last Market History", marketHistory)
            }
        }
    }
}


@Composable
private fun MarketplaceRow(
    context: Context,
    title: String,
    items: Iterable<ArtCollectibleForSale>
) {
    Column(modifier = Modifier
        .padding(vertical = 10.dp, horizontal = 10.dp)) {
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
        ArtCollectibleForSaleList(context, items)
    }
}

@Composable
private fun ArtCollectibleForSaleList(
    context: Context,
    items: Iterable<ArtCollectibleForSale>
) {
    LazyRow(
        modifier = Modifier.padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(Iterables.size(items)) { idx ->
            ArtCollectibleForSaleCard(context, Iterables.get(items, idx))
        }
    }
}