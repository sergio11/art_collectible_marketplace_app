package com.dreamsoftware.artcollectibles.ui.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible

sealed class DestinationItem(var route: String, arguments: List<NamedNavArgument> = emptyList()) {
    object OnBoarding : DestinationItem(route = "onBoarding")
    object SignIn : DestinationItem(route = "signIn")
    object SignUp : DestinationItem(route = "signUp")
    object Home : DestinationItem(route = "home")
    object MyTokens : DestinationItem(route = "myTokens")
    object Add : DestinationItem(route = "add")
    object Search : DestinationItem(route = "search")
    object Profile : DestinationItem(route = "profile")
    object TokenDetail : DestinationItem(route = "token/detail/{id}", arguments = listOf(
        navArgument("id") {
            type = NavType.StringType
        }
    )) {
        fun buildRoute(artCollectible: ArtCollectible): String =
            route.replace(
                oldValue = "{id}",
                newValue = "${artCollectible.id}"
            )
    }
}


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