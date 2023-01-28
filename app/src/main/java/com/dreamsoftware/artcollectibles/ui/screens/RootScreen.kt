package com.dreamsoftware.artcollectibles.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.artcollectibles.ui.navigations.NavigationItem
import com.dreamsoftware.artcollectibles.ui.screens.account.onboarding.OnBoardingScreen
import com.dreamsoftware.artcollectibles.ui.screens.account.signin.SignInScreen
import com.dreamsoftware.artcollectibles.ui.screens.account.signup.SignUpScreen
import com.dreamsoftware.artcollectibles.ui.screens.add.AddNftScreen
import com.dreamsoftware.artcollectibles.ui.screens.home.HomeScreen
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.MyTokensScreen
import com.dreamsoftware.artcollectibles.ui.screens.profile.ProfileScreen
import com.dreamsoftware.artcollectibles.ui.screens.search.SearchScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun RootScreen(
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = NavigationItem.OnBoarding.route) {
        composable(NavigationItem.OnBoarding.route) {
            OnBoardingScreen(
                onUserAlreadyAuthenticated = {
                    navigationController.navigate(NavigationItem.Home.route) {
                        popUpTo(NavigationItem.Home.route)
                    }
                },
                onNavigateToLogin = {
                    navigationController.navigate(NavigationItem.SignIn.route)
                },
                onNavigateToSignUp = {
                    navigationController.navigate(NavigationItem.SignUp.route)
                }
            )
        }
        composable(NavigationItem.SignIn.route) {
            SignInScreen {
                navigationController.navigate(NavigationItem.Home.route) {
                    popUpTo(NavigationItem.Home.route)
                }
            }
        }
        composable(NavigationItem.SignUp.route) {
            SignUpScreen {
                navigationController.popBackStack()
            }
        }
        composable(NavigationItem.Home.route) {
            HomeScreen(navigationController)
        }
        composable(NavigationItem.Stats.route) {
            MyTokensScreen(navigationController)
        }
        composable(NavigationItem.Add.route) {
            AddNftScreen(navigationController)
        }
        composable(NavigationItem.Search.route) {
            SearchScreen(navigationController)
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(navigationController) {
                navigationController.navigate(NavigationItem.OnBoarding.route) {
                    popUpTo(NavigationItem.OnBoarding.route)
                }
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