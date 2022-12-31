package com.dreamsoftware.artcollectibles.ui.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object OnBoarding : NavigationItem("onBoarding", Icons.Filled.Home, "OnBoarding")
    object SignIn : NavigationItem("signIn", Icons.Filled.Home, "SignIn")
    object SignUp : NavigationItem("signUp", Icons.Filled.Home, "SignUp")
    object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    object Stats : NavigationItem("stats", Icons.Filled.Check, "Stats")
    object Add : NavigationItem("add", Icons.Filled.Add, "Add")
    object Search : NavigationItem("search", Icons.Filled.Search, "Search")
    object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")
}