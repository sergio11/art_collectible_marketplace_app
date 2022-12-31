package com.dreamsoftware.artcollectibles.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.artcollectibles.ui.navigations.NavigationItem
import com.dreamsoftware.artcollectibles.ui.screens.home.HomeScreen
import com.dreamsoftware.artcollectibles.ui.screens.onboarding.OnBoardingScreen
import com.dreamsoftware.artcollectibles.ui.screens.signin.SignInScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = NavigationItem.OnBoarding.route) {
        composable(NavigationItem.OnBoarding.route) {
            OnBoardingScreen {
                navigationController.navigate(NavigationItem.SignIn.route)
            }
        }
        composable(NavigationItem.SignIn.route) {
            SignInScreen {
                navigationController.navigate(NavigationItem.Home.route)
            }
        }
        composable(NavigationItem.SignUp.route) {
            Text("SignUp")
        }
        composable(NavigationItem.Home.route) {
            HomeScreen(navigationController)
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

@Preview
@Composable
fun RootScreenPreview() {
    ArtCollectibleMarketplaceTheme {
        RootScreen()
    }
}