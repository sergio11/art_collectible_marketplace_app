package com.dreamsoftware.artcollectibles.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.artcollectibles.ui.navigations.BottomBar
import com.dreamsoftware.artcollectibles.ui.navigations.NavigationItem
import com.dreamsoftware.artcollectibles.ui.screens.home.HomeScreen
import com.dreamsoftware.artcollectibles.ui.screens.onboarding.OnBoardingScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
) {
    val navigationController = rememberNavController()
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            if (!shouldShowOnBoarding) {
                BottomBar(navController = navigationController)
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navigationController,
            startDestination = if (shouldShowOnBoarding) {
                NavigationItem.Login.route
            } else {
                NavigationItem.Stats.route
            }) {
            composable(NavigationItem.Login.route) {
                OnBoardingScreen(
                    onNavigateAction = {
                        navigationController.navigate(NavigationItem.Home.route)
                        shouldShowOnBoarding = false
                    }
                )
            }
            composable(NavigationItem.Home.route) {
                HomeScreen()
            }
            composable(NavigationItem.Stats.route) {
                Text("Stats")
            }
            composable(NavigationItem.Add.route) {
                Text("Add")
            }
            composable(NavigationItem.Search.route) {
                Text("Search")
            }
            composable(NavigationItem.Profile.route) {
                Text("Profile")
            }
        }
    }
}

@Preview
@Composable
fun RootScreenPreview() {
    ArtCollectibleMarketplaceTheme {
        RootScreen()
    }
}