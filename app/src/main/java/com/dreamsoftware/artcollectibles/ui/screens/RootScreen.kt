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
import com.dreamsoftware.artcollectibles.ui.screens.availableitems.AvailableMarketItemsScreen
import com.dreamsoftware.artcollectibles.ui.screens.categorydetail.CategoryDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.commentdetail.CommentDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.comments.CommentsScreen
import com.dreamsoftware.artcollectibles.ui.screens.favorites.FavoritesScreen
import com.dreamsoftware.artcollectibles.ui.screens.followers.FollowersScreen
import com.dreamsoftware.artcollectibles.ui.screens.home.HomeScreen
import com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail.MarketItemDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.MyTokensScreen
import com.dreamsoftware.artcollectibles.ui.screens.profile.ProfileScreen
import com.dreamsoftware.artcollectibles.ui.screens.search.SearchScreen
import com.dreamsoftware.artcollectibles.ui.screens.sellingitems.SellingMarketItemsScreen
import com.dreamsoftware.artcollectibles.ui.screens.tokendetail.TokenDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.tokenhistory.TokenHistoryScreen
import com.dreamsoftware.artcollectibles.ui.screens.tokens.TokensScreen
import com.dreamsoftware.artcollectibles.ui.screens.visitors.VisitorsScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme

@Composable
fun RootScreen(
) {
    val navigationController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = DestinationItem.OnBoarding.route
    ) {
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
            with(navigationController) {
                HomeScreen(this, onGoToMarketItemDetail = {
                    navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                }, onGoToCategoryDetail = {
                    navigate(DestinationItem.CategoryDetail.buildRoute(it))
                }, onGoToUserDetail = {
                    navigate(DestinationItem.ArtistDetail.buildRoute(it))
                }, onGoToTokenDetail = {
                    navigate(DestinationItem.TokenDetail.buildRoute(it))
                }, onGoToMarketHistoryItemDetail = {
                    navigate(DestinationItem.MarketItemDetail.buildHistoryMarketItemRoute(it))
                }, onShowAvailableMarketItems = {
                    navigate(DestinationItem.AvailableMarketItems.route)
                }, onShowSellingMarketItems = {
                    navigate(DestinationItem.SellingMarketItems.route)
                })
            }
        }
        composable(DestinationItem.AvailableMarketItems.route) {
            AvailableMarketItemsScreen {
                navigationController.navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
            }
        }
        composable(DestinationItem.SellingMarketItems.route) {
            SellingMarketItemsScreen {
                navigationController.navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
            }
        }
        composable(DestinationItem.MyTokens.route) {
            MyTokensScreen(navigationController) {
                navigationController.navigate(DestinationItem.TokenDetail.buildRoute(it))
            }
        }
        composable(DestinationItem.Add.route) {
            AddNftScreen {
                navigationController.popBackStack()
            }
        }
        composable(DestinationItem.Explore.route) {
            SearchScreen(navigationController) {
                navigationController.navigate(DestinationItem.ArtistDetail.buildRoute(it))
            }
        }
        composable(DestinationItem.UserFollowers.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.UserFollowers.parseArgs(args)?.let { screenArgs ->
                    FollowersScreen(screenArgs) {
                        navigationController.navigate(DestinationItem.ArtistDetail.buildRoute(it))
                    }
                }
            }
        }
        composable(DestinationItem.UserTokens.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.UserTokens.parseArgs(args)?.let { screenArgs ->
                    TokensScreen(screenArgs) {
                        navigationController.navigate(DestinationItem.TokenDetail.buildRoute(it))
                    }
                }
            }
        }
        composable(DestinationItem.Profile.route) {
            with(navigationController) {
                ProfileScreen(navigationController, onSessionClosed = {
                    navigate(DestinationItem.OnBoarding.route) {
                        popUpTo(DestinationItem.OnBoarding.route)
                    }
                }, onOpenProfile = {
                    navigate(DestinationItem.ArtistDetail.buildRoute(it))
                })
            }
        }
        composable(DestinationItem.TokenDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.TokenDetail.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        TokenDetailScreen(screenArgs, onSeeArtistDetail = {
                            navigate(DestinationItem.ArtistDetail.buildRoute(it))
                        }, onTokenBurned = {
                            popBackStack()
                        }, onSeeCommentsByToken = {
                            navigate(DestinationItem.CommentsList.buildRoute(it))
                        }, onSeeLikesByToken = {
                            navigate(DestinationItem.FavoriteList.buildRoute(it))
                        }, onSeeVisitorsByToken = {
                            navigate(DestinationItem.VisitorsList.buildRoute(it))
                        }, onSeeCommentDetail = {
                            navigate(DestinationItem.CommentDetail.buildRoute(it))
                        }, onSeeTokenHistory = {
                            navigate(DestinationItem.TokenHistoryList.buildRoute(it))
                        }, onSeeMarketItemDetail = {
                            navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                        }, onSeeTokenDetail = {
                            navigate(DestinationItem.TokenDetail.buildRoute(it))
                        }, onBackClicked = {
                            popBackStack()
                        })
                    }
                }
            }
        }
        composable(DestinationItem.CommentsList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.CommentsList.parseArgs(args)?.let { screenArgs ->
                    CommentsScreen(args = screenArgs, onSeeCommentDetail = {
                        navigationController.navigate(DestinationItem.CommentDetail.buildRoute(it))
                    })
                }
            }
        }
        composable(DestinationItem.CommentDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.CommentDetail.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        CommentDetailScreen(args = screenArgs, onCommentDeleted = {
                            popBackStack()
                        }, onOpenArtistDetailCalled = {
                            navigate(DestinationItem.ArtistDetail.buildRoute(it))
                        }, onShowTokensCreatedBy = {
                            navigate(DestinationItem.UserTokens.buildShowTokensCreatedRoute(it))
                        }, onGoToTokenDetail = {
                            navigate(DestinationItem.TokenDetail.buildRoute(it))
                        }, onBackClicked = {
                            popBackStack()
                        })
                    }
                }
            }
        }
        composable(DestinationItem.FavoriteList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.FavoriteList.parseArgs(args)?.let { screenArgs ->
                    FavoritesScreen(args = screenArgs) {
                        navigationController.navigate(DestinationItem.ArtistDetail.buildRoute(it))
                    }
                }
            }
        }
        composable(DestinationItem.VisitorsList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.VisitorsList.parseArgs(args)?.let { screenArgs ->
                    VisitorsScreen(args = screenArgs) {
                        navigationController.navigate(DestinationItem.ArtistDetail.buildRoute(it))
                    }
                }
            }
        }
        composable(DestinationItem.TokenHistoryList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.TokenHistoryList.parseArgs(args)?.let { screenArgs ->
                    TokenHistoryScreen(args = screenArgs)
                }
            }
        }
        composable(DestinationItem.CategoryDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.CategoryDetail.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        CategoryDetailScreen(
                            args = screenArgs,
                            onShowTokenForSaleDetail = {
                                navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                            }, onBackClicked = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }
        composable(DestinationItem.ArtistDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.ArtistDetail.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        ArtistDetailScreen(screenArgs, onShowFollowers = {
                            navigate(DestinationItem.UserFollowers.buildFollowersRoute(it))
                        }, onShowFollowing = {
                            navigate(DestinationItem.UserFollowers.buildFollowingRoute(it))
                        }, onGoToTokenDetail = {
                            navigate(DestinationItem.TokenDetail.buildRoute(it))
                        }, onShowTokensOwnedBy = {
                            navigate(DestinationItem.UserTokens.buildShowTokensOwnedRoute(it))
                        }, onShowTokensCreatedBy = {
                            navigate(DestinationItem.UserTokens.buildShowTokensCreatedRoute(it))
                        }, onBackClicked = {
                            popBackStack()
                        })
                    }
                }
            }
        }
        composable(DestinationItem.MarketItemDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.MarketItemDetail.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        MarketItemDetailScreen(screenArgs, onOpenArtistDetailCalled = {
                            navigate(DestinationItem.ArtistDetail.buildRoute(it))
                        }, onOpenTokenDetailCalled = {
                            navigate(DestinationItem.TokenDetail.buildRoute(it))
                        }, onBackClicked = {
                            popBackStack()
                        }, onOpenMarketItemDetail = {
                            navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                        })
                    }
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