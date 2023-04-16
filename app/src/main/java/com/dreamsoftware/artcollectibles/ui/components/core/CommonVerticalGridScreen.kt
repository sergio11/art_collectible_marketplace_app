package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import com.google.common.collect.Iterables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: Any>CommonVerticalGridScreen(
    lazyGridState: LazyGridState,
    isLoading: Boolean,
    items: Iterable<T>,
    appBarTitle: String,
    onBuildContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    LoadingDialog(isShowingDialog = isLoading)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        appBarTitle,
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
                    items(Iterables.size(items)) { index ->
                        onBuildContent(item = Iterables.get(items, index))
                    }
                }
            }
        }
    }
}