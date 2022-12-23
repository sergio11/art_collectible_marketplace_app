package com.dreamsoftware.artcollectibles.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleCategoryList
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleList
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Art Collectible Marketplace",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent))
        },
        containerColor = Color(33, 17, 52)
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Category List
            ArtCollectibleCategoryList()

            Text(
                "New collectibles",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

            ArtCollectibleList()

            Text(
                "Top seller",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

            ArtCollectibleList()
        }
    }
}

@Composable
@Preview
fun PreviewHomeScreen() {
    ArtCollectibleMarketplaceTheme {
        HomeScreen()
    }
}