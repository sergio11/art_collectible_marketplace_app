package com.dreamsoftware.artcollectibles.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dreamsoftware.artcollectibles.ui.navigation.DestinationItem
import com.dreamsoftware.artcollectibles.ui.screens.account.onboarding.OnBoardingScreen
import com.dreamsoftware.artcollectibles.ui.screens.account.signin.SignInScreen
import com.dreamsoftware.artcollectibles.ui.screens.account.signup.SignUpScreen
import com.dreamsoftware.artcollectibles.ui.screens.add.AddNftScreen
import com.dreamsoftware.artcollectibles.ui.screens.artistdetail.ArtistDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.home.HomeScreen
import com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail.MarketItemDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.MyTokensScreen
import com.dreamsoftware.artcollectibles.ui.screens.profile.ProfileScreen
import com.dreamsoftware.artcollectibles.ui.screens.search.SearchScreen
import com.dreamsoftware.artcollectibles.ui.screens.tokendetail.TokenDetailScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun RootScreen(
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = DestinationItem.OnBoarding.route) {
        composable(DestinationItem.OnBoarding.route) {
            OnBoardingScreen(
                onUserAlreadyAuthenticated = {
                    navigationController.navigate(DestinationItem.Home.route) {
                        popUpTo(DestinationItem.Home.route)
                    }
                },
                onNavigateToLogin = {
                    navigationController.navigate(DestinationItem.SignIn.route)
                },
                onNavigateToSignUp = {
                    navigationController.navigate(DestinationItem.SignUp.route)
                }
            )
        }
        composable(DestinationItem.SignIn.route) {
            SignInScreen {
                navigationController.navigate(DestinationItem.Home.route) {
                    popUpTo(DestinationItem.Home.route)
                }
            }
        }
        composable(DestinationItem.SignUp.route) {
            SignUpScreen {
                navigationController.popBackStack()
            }
        }
        composable(DestinationItem.Home.route) {
            HomeScreen(navigationController) {
                navigationController.navigate(DestinationItem.MarketItemDetail.buildRoute(it))
            }
        }
        composable(DestinationItem.MyTokens.route) {
            MyTokensScreen(navigationController) {
                navigationController.navigate(DestinationItem.TokenDetail.buildRoute(it))
            }
        }
        composable(DestinationItem.Add.route) {
            AddNftScreen(navigationController) {
                navigationController.popBackStack()
            }
        }
        composable(DestinationItem.Search.route) {
            SearchScreen(navigationController) {
                navigationController.navigate(DestinationItem.ArtistDetail.buildRoute(it))
            }
        }
        composable(DestinationItem.Profile.route) {
            ProfileScreen(navigationController) {
                navigationController.navigate(DestinationItem.OnBoarding.route) {
                    popUpTo(DestinationItem.OnBoarding.route)
                }
            }
        }
        composable(DestinationItem.TokenDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.TokenDetail.parseArgs(args)?.let { screenArgs ->
                    TokenDetailScreen(navigationController, screenArgs) {
                        navigationController.popBackStack()
                    }
                }
            }
        }
        composable(DestinationItem.ArtistDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.ArtistDetail.parseArgs(args)?.let { screenArgs ->
                    ArtistDetailScreen(navigationController, screenArgs)
                }
            }
        }
        composable(DestinationItem.MarketItemDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.MarketItemDetail.parseArgs(args)?.let { screenArgs ->
                    MarketItemDetailScreen(navigationController, screenArgs)
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