package com.dreamsoftware.artcollectibles.ui.navigation

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.screens.artistdetail.ArtistDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.marketitemdetail.MarketItemDetailScreenArgs
import com.dreamsoftware.artcollectibles.ui.screens.tokendetail.TokenDetailScreenArgs
import java.math.BigInteger

sealed class DestinationItem(var route: String, arguments: List<NamedNavArgument> = emptyList()) {
    object OnBoarding : DestinationItem(route = "onBoarding")
    object SignIn : DestinationItem(route = "signIn")
    object SignUp : DestinationItem(route = "signUp")
    object Home : DestinationItem(route = "home")
    object MyTokens : DestinationItem(route = "myTokens")
    object Add : DestinationItem(route = "add")
    object Search : DestinationItem(route = "search")
    object Profile : DestinationItem(route = "profile")
    object TokenDetail : DestinationItem(route = "tokens/detail/{id}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(artCollectible: ArtCollectible): String =
            route.replace(
                oldValue = "{id}",
                newValue = "${artCollectible.id}"
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

        fun buildRoute(userInfo: UserInfo): String =
            route.replace(
                oldValue = "{uid}",
                newValue = userInfo.uid
            )

        fun parseArgs(args: Bundle): ArtistDetailScreenArgs? = with(args) {
            getString("uid")?.let {
                ArtistDetailScreenArgs(uid = it)
            }
        }
    }
    object MarketItemDetail : DestinationItem(route = "market/detail/{id}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {

        fun buildRoute(artCollectibleForSale: ArtCollectibleForSale): String =
            route.replace(
                oldValue = "{id}",
                newValue = "${artCollectibleForSale.token.id}"
            )

        fun parseArgs(args: Bundle): MarketItemDetailScreenArgs? = with(args) {
            getString("id")?.toLongOrNull()?.let {
                MarketItemDetailScreenArgs(tokenId = BigInteger.valueOf(it))
            }
        }
    }
}

// MarketItemDetailScreen
sealed class NavigationItem(
    var destination: DestinationItem,
    var icon: ImageVector,
    var title: String
) {
    object Home :
        NavigationItem(destination = DestinationItem.Home, icon = Icons.Filled.Home, title = "Home")

    object MyTokens : NavigationItem(
        destination = DestinationItem.MyTokens,
        icon = Icons.Filled.Check,
        title = "MyTokens"
    )

    object Add :
        NavigationItem(destination = DestinationItem.Add, icon = Icons.Filled.Add, title = "Add")

    object Search : NavigationItem(
        destination = DestinationItem.Search,
        icon = Icons.Filled.Search,
        title = "Search"
    )

    object Profile : NavigationItem(
        destination = DestinationItem.Profile,
        icon = Icons.Filled.Person,
        title = "Profile"
    )
}