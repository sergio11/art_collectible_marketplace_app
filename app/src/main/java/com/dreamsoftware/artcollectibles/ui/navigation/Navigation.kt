package com.dreamsoftware.artcollectibles.ui.navigation

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.*
import com.dreamsoftware.artcollectibles.ui.screens.artistdetail.ArtistDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.categorydetail.CategoryDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.commentdetail.CommentDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.comments.CommentsScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.edit.EditNftScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.favorites.FavoritesScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.followers.FollowersScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail.MarketItemDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.tokendetail.TokenDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.tokenhistory.TokenHistoryScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.tokens.TokensScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.visitors.VisitorsScreenArgs
import java.math.BigInteger

sealed class DestinationItem(var route: String, arguments: List<NamedNavArgument> = emptyList()) {
    object OnBoarding : DestinationItem(route = "onBoarding")
    object SignIn : DestinationItem(route = "signIn")
    object SignUp : DestinationItem(route = "signUp")
    object Home : DestinationItem(route = "home")
    object MyTokens : DestinationItem(route = "myTokens")
    object Mint : DestinationItem(route = "mint")
    object Explore : DestinationItem(route = "explore")
    object Profile : DestinationItem(route = "profile")
    object AvailableMarketItems: DestinationItem(route = "availableMarketItems")
    object SellingMarketItems : DestinationItem(route = "sellingMarketItems")
    object MarketHistory: DestinationItem(route = "marketHistory")
    object MarketStatistics: DestinationItem(route = "marketStatistics")
    object TokenDetail : DestinationItem(route = "tokens/detail/{id}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(tokenId: BigInteger): String =
            route.replace(
                oldValue = "{id}",
                newValue = "$tokenId"
            )

        fun parseArgs(args: Bundle): TokenDetailScreenArgs? = with(args) {
            getString("id")?.toLongOrNull()?.let {
                TokenDetailScreenArgs(
                    tokenId = BigInteger.valueOf(it)
                )
            }
        }
    }

    object ArtistDetail : DestinationItem(route = "artists/detail/{uid}", arguments = listOf(
        navArgument("uid") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(userUid: String): String =
            route.replace(
                oldValue = "{uid}",
                newValue = userUid
            )

        fun parseArgs(args: Bundle): ArtistDetailScreenArgs? = with(args) {
            getString("uid")?.let {
                ArtistDetailScreenArgs(uid = it)
            }
        }
    }

    object MarketItemDetail : DestinationItem(route = "market/detail/{type}/{id}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        },
        navArgument("type") {
            type = NavType.StringType
        }
    )) {

        private const val SHOW_HISTORY_MARKET_ITEM = "HISTORY"
        private const val SHOW_FOR_SALE_MARKET_ITEM = "FOR_SALE"

        fun buildHistoryMarketItemRoute(marketItemId: BigInteger): String =
            buildRoute(marketItemId, SHOW_HISTORY_MARKET_ITEM)

        fun buildForSaleMarketItemRoute(tokenId: BigInteger): String =
            buildRoute(tokenId, SHOW_FOR_SALE_MARKET_ITEM)

        fun parseArgs(args: Bundle): MarketItemDetailScreenArgs? = with(args) {
            getString("id")?.toBigInteger()?.let { id ->
                getString("type")?.let { type ->
                    MarketItemDetailScreenArgs(
                        id = id,
                        viewType = if (type == SHOW_HISTORY_MARKET_ITEM) {
                            MarketItemDetailScreenArgs.ViewTypeEnum.HISTORY
                        } else {
                            MarketItemDetailScreenArgs.ViewTypeEnum.FOR_SALE
                        }
                    )
                }
            }
        }

        private fun buildRoute(id: BigInteger, type: String): String =
            route.replace(
                oldValue = "{id}",
                newValue = "$id"
            ).replace(
                oldValue = "{type}",
                newValue = type
            )
    }

    object UserFollowers : DestinationItem(route = "users/{id}/{type}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        },
        navArgument("type") {
            type = NavType.StringType
        }
    )) {

        private const val SHOW_FOLLOWERS = "FOLLOWERS"
        private const val SHOW_FOLLOWING = "FOLLOWING"

        fun buildFollowersRoute(userUid: String): String =
            buildRoute(userUid, SHOW_FOLLOWERS)

        fun buildFollowingRoute(userUid: String): String =
            buildRoute(userUid, SHOW_FOLLOWING)

        fun parseArgs(args: Bundle): FollowersScreenArgs? = with(args) {
            getString("id")?.let { userUid ->
                getString("type")?.let { type ->
                    FollowersScreenArgs(
                        userUid = userUid,
                        viewType = if (type == SHOW_FOLLOWERS) {
                            FollowersScreenArgs.ViewTypeEnum.FOLLOWERS
                        } else {
                            FollowersScreenArgs.ViewTypeEnum.FOLLOWING
                        }
                    )
                }
            }
        }

        private fun buildRoute(userUid: String, type: String): String =
            route.replace(
                oldValue = "{id}",
                newValue = userUid
            ).replace(
                oldValue = "{type}",
                newValue = type
            )
    }

    object UserTokens : DestinationItem(route = "users/{id}/tokens/{type}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        },
        navArgument("type") {
            type = NavType.StringType
        }
    )) {

        private const val SHOW_TOKENS_OWNED = "TOKENS_OWNED"
        private const val SHOW_TOKENS_CREATED = "TOKENS_CREATED"

        fun buildShowTokensOwnedRoute(userUid: String): String =
            buildRoute(userUid, SHOW_TOKENS_OWNED)

        fun buildShowTokensCreatedRoute(userUid: String): String =
            buildRoute(userUid, SHOW_TOKENS_CREATED)

        fun parseArgs(args: Bundle): TokensScreenArgs? = with(args) {
            getString("id")?.let { userAddress ->
                getString("type")?.let { type ->
                    TokensScreenArgs(
                        userAddress = userAddress,
                        viewType = if (type == SHOW_TOKENS_OWNED) {
                            TokensScreenArgs.ViewTypeEnum.OWNED
                        } else {
                            TokensScreenArgs.ViewTypeEnum.CREATED
                        }
                    )
                }
            }
        }

        private fun buildRoute(userUid: String, type: String): String =
            route.replace(
                oldValue = "{id}",
                newValue = userUid
            ).replace(
                oldValue = "{type}",
                newValue = type
            )
    }

    object CategoryDetail : DestinationItem(route = "categories/{id}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(artCollectibleCategory: ArtCollectibleCategory): String =
            route.replace(
                oldValue = "{id}",
                newValue = artCollectibleCategory.uid
            )

        fun parseArgs(args: Bundle): CategoryDetailScreenArgs? = with(args) {
            getString("id")?.let {
                CategoryDetailScreenArgs(uid = it)
            }
        }
    }
    object CommentsList : DestinationItem(route = "token/{id}/comments", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(tokenId: BigInteger): String =
            route.replace(
                oldValue = "{id}",
                newValue = tokenId.toString()
            )

        fun parseArgs(args: Bundle): CommentsScreenArgs? = with(args) {
            getString("id")?.toBigInteger()?.let {
                CommentsScreenArgs(
                    tokenId = it
                )
            }
        }
    }

    object CommentDetail : DestinationItem(route = "token/{tokenId}/comments/{commentId}", arguments = listOf(
        navArgument("tokenId") {
            type = NavType.StringType
        },
        navArgument("commentId") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(comment: Comment): String =
            route.replace(
                oldValue = "{tokenId}",
                newValue = comment.tokenId.toString()
            ).replace(
                oldValue = "{commentId}",
                newValue = comment.uid
            )

        fun parseArgs(args: Bundle): CommentDetailScreenArgs? = with(args) {
            getString("commentId")?.let {
                CommentDetailScreenArgs(uid = it)
            }
        }
    }

    object EditTokenMetadata : DestinationItem(route = "token/{cid}/edit", arguments = listOf(
        navArgument("cid") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(cid: String): String =
            route.replace(
                oldValue = "{cid}",
                newValue = cid
            )

        fun parseArgs(args: Bundle): EditNftScreenArgs? = with(args) {
            getString("cid")?.let {
                EditNftScreenArgs(metadataCid = it)
            }
        }
    }


    object FavoriteList : DestinationItem(route = "token/{id}/likes", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(tokenId: BigInteger): String =
            route.replace(
                oldValue = "{id}",
                newValue = tokenId.toString()
            )

        fun parseArgs(args: Bundle): FavoritesScreenArgs? = with(args) {
            getString("id")?.toBigInteger()?.let {
                FavoritesScreenArgs(
                    tokenId = it
                )
            }
        }
    }

    object VisitorsList : DestinationItem(route = "token/{id}/visitors", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(tokenId: BigInteger): String =
            route.replace(
                oldValue = "{id}",
                newValue = tokenId.toString()
            )

        fun parseArgs(args: Bundle): VisitorsScreenArgs? = with(args) {
            getString("id")?.toBigInteger()?.let {
                VisitorsScreenArgs(
                    tokenId = it
                )
            }
        }
    }

    object TokenHistoryList : DestinationItem(route = "token/{id}/history", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(tokenId: BigInteger): String =
            route.replace(
                oldValue = "{id}",
                newValue = tokenId.toString()
            )

        fun parseArgs(args: Bundle): TokenHistoryScreenArgs? = with(args) {
            getString("id")?.toLongOrNull()?.let {
                TokenHistoryScreenArgs(
                    tokenId = BigInteger.valueOf(it)
                )
            }
        }
    }

}

// MarketItemDetailScreen
sealed class NavigationItem(
    var destination: DestinationItem,
    @DrawableRes var iconRes: Int,
    var title: String
) {
    object Home :
        NavigationItem(
            destination = DestinationItem.Home,
            iconRes = R.drawable.home_menu_item_icon,
            title = "Home"
        )

    object MyTokens : NavigationItem(
        destination = DestinationItem.MyTokens,
        iconRes = R.drawable.my_nft_menu_item_icon,
        title = "MyTokens"
    )

    object Add :
        NavigationItem(
            destination = DestinationItem.Mint,
            iconRes = R.drawable.add_menu_item_icon,
            title = "Add"
        )

    object Explore : NavigationItem(
        destination = DestinationItem.Explore,
        iconRes  = R.drawable.explore_menu_item_icon,
        title = "Search"
    )

    object Profile : NavigationItem(
        destination = DestinationItem.Profile,
        iconRes  = R.drawable.profile_menu_item_icon,
        title = "Profile"
    )
}