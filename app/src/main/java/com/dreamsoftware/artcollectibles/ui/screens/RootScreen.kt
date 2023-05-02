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
import com.dreamsoftware.artcollectibles.ui.screens.mint.MintNftScreen
import com.dreamsoftware.artcollectibles.ui.screens.artistdetail.ArtistDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.availableitems.AvailableMarketItemsScreen
import com.dreamsoftware.artcollectibles.ui.screens.categorydetail.CategoryDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.commentdetail.CommentDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.comments.CommentsScreen
import com.dreamsoftware.artcollectibles.ui.screens.edit.EditNftScreen
import com.dreamsoftware.artcollectibles.ui.screens.favorites.FavoritesScreen
import com.dreamsoftware.artcollectibles.ui.screens.followers.FollowersScreen
import com.dreamsoftware.artcollectibles.ui.screens.home.HomeScreen
import com.dreamsoftware.artcollectibles.ui.screens.markethistory.MarketHistoryScreen
import com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail.MarketItemDetailScreen
import com.dreamsoftware.artcollectibles.ui.screens.marketstatistics.MarketStatisticsScreen
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.MyTokensScreen
import com.dreamsoftware.artcollectibles.ui.screens.notifications.NotificationsScreen
import com.dreamsoftware.artcollectibles.ui.screens.preferences.PreferencesScreen
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
                onGoToLogin = {
                    navigationController.navigate(DestinationItem.SignIn.route)
                },
                onGoToSignUp = {
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
            with(navigationController) {
                SignUpScreen(
                    onSignUpSuccess = {
                        popBackStack()
                    },
                    onGoToSignIn = {
                        navigate(DestinationItem.SignIn.route)
                    }
                )
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
                }, onGoToAvailableMarketItems = {
                    navigate(DestinationItem.AvailableMarketItems.route)
                }, onGoToSellingMarketItems = {
                    navigate(DestinationItem.SellingMarketItems.route)
                }, onGoToMarketHistory = {
                    navigate(DestinationItem.MarketHistory.route)
                }, onGoToMarketStatistics = {
                    navigate(DestinationItem.MarketStatistics.route)
                }, onGoToNotifications = {
                    navigate(DestinationItem.Notifications.route)
                })
            }
        }
        composable(DestinationItem.AvailableMarketItems.route) {
            with(navigationController) {
                AvailableMarketItemsScreen(
                    onMarketItemSelected = {
                        navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }
        composable(DestinationItem.SellingMarketItems.route) {
            with(navigationController) {
                SellingMarketItemsScreen(
                    onMarketItemSelected = {
                        navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }
        composable(DestinationItem.MarketHistory.route) {
            with(navigationController) {
                MarketHistoryScreen(
                    onGoToMarketItemHistoryDetail = {
                        navigate(DestinationItem.MarketItemDetail.buildHistoryMarketItemRoute(it))
                    },
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }
        composable(DestinationItem.Notifications.route) {
            with(navigationController) {
                NotificationsScreen(
                    onGoToNotificationDetail = {},
                    onBackPressed = {
                        popBackStack()
                    }
                )
            }
        }
        composable(DestinationItem.Preferences.route) {
            PreferencesScreen {
                navigationController.popBackStack()
            }
        }
        composable(DestinationItem.MarketStatistics.route) {
            MarketStatisticsScreen {
                navigationController.popBackStack()
            }
        }
        composable(DestinationItem.MyTokens.route) {
            MyTokensScreen(navigationController) {
                navigationController.navigate(DestinationItem.TokenDetail.buildRoute(it))
            }
        }
        composable(DestinationItem.Mint.route) {
            MintNftScreen {
                navigationController.popBackStack()
            }
        }
        composable(DestinationItem.EditTokenMetadata.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.EditTokenMetadata.parseArgs(args)?.let { screenArgs ->
                    EditNftScreen(screenArgs) {
                        navigationController.popBackStack()
                    }
                }
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
                    with(navigationController) {
                        FollowersScreen(
                            args = screenArgs,
                            onGoToArtistDetail = {
                                navigate(DestinationItem.ArtistDetail.buildRoute(it))
                            },
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }
        composable(DestinationItem.UserTokens.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.UserTokens.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        TokensScreen(
                            args = screenArgs,
                            onGoToTokenDetail = {
                                navigate(DestinationItem.TokenDetail.buildRoute(it))
                            },
                            onBackPressed = {
                                popBackStack()
                            }
                        )
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
                }, onGoToUserProfile = {
                    navigate(DestinationItem.ArtistDetail.buildRoute(it))
                }, onGoToUserPreferences = {
                    navigate(DestinationItem.Preferences.route)
                })
            }
        }
        composable(DestinationItem.TokenDetail.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.TokenDetail.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        TokenDetailScreen(screenArgs, onGoToArtistDetail = {
                            navigate(DestinationItem.ArtistDetail.buildRoute(it))
                        }, onTokenBurned = {
                            popBackStack()
                        }, onGoToCommentsByToken = {
                            navigate(DestinationItem.CommentsList.buildRoute(it))
                        }, onGoToLikesByToken = {
                            navigate(DestinationItem.FavoriteList.buildRoute(it))
                        }, onGoToVisitorsByToken = {
                            navigate(DestinationItem.VisitorsList.buildRoute(it))
                        }, onGoToCommentDetail = {
                            navigate(DestinationItem.CommentDetail.buildRoute(it))
                        }, onGoToTokenHistory = {
                            navigate(DestinationItem.TokenHistoryList.buildRoute(it))
                        }, onGoToMarketItemDetail = {
                            navigate(DestinationItem.MarketItemDetail.buildForSaleMarketItemRoute(it))
                        }, onGoToTokenDetail = {
                            navigate(DestinationItem.TokenDetail.buildRoute(it))
                        }, onBackClicked = {
                            popBackStack()
                        }, onGoToEditToken = {
                            navigate(DestinationItem.EditTokenMetadata.buildRoute(it))
                        }, onGoToMarketHistoryItemDetail = {
                            navigate(DestinationItem.MarketItemDetail.buildHistoryMarketItemRoute(it))
                        })
                    }
                }
            }
        }
        composable(DestinationItem.CommentsList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.CommentsList.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        CommentsScreen(
                            args = screenArgs,
                            onGoToCommentDetail = {
                                navigate(DestinationItem.CommentDetail.buildRoute(it))
                            },
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
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
                    with(navigationController) {
                        FavoritesScreen(
                            args = screenArgs,
                            onGoToArtistDetail = {
                                navigate(DestinationItem.ArtistDetail.buildRoute(it))
                            }, onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }
        composable(DestinationItem.VisitorsList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.VisitorsList.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        VisitorsScreen(
                            args = screenArgs,
                            onGoToArtistDetail = {
                                navigate(DestinationItem.ArtistDetail.buildRoute(it))
                            },
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
                }
            }
        }
        composable(DestinationItem.TokenHistoryList.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { args ->
                DestinationItem.TokenHistoryList.parseArgs(args)?.let { screenArgs ->
                    with(navigationController) {
                        TokenHistoryScreen(
                            args = screenArgs,
                            onGoToMarketItemHistoryDetail = {
                                navigate(DestinationItem.MarketItemDetail.buildHistoryMarketItemRoute(it))
                            },
                            onBackPressed = {
                                popBackStack()
                            }
                        )
                    }
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