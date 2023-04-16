package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
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
fun <T: Any>CommonVerticalColumnScreen(
    lazyListState: LazyListState,
    isLoading: Boolean,
    items: Iterable<T>,
    appBarTitle: String,
    onBuildContent: @Composable LazyItemScope.(item: T) -> Unit
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
                LazyColumn(
                    modifier = Modifier.padding(8.dp),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(Iterables.size(items)) { index ->
                        onBuildContent(item = Iterables.get(items, index))
                    }
                }
            }
        }
    }
}