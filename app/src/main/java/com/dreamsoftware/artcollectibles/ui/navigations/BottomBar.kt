package com.dreamsoftware.artcollectibles.ui.navigations

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dreamsoftware.artcollectibles.ui.theme.Purple40

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.MyTokens,
        NavigationItem.Add,
        NavigationItem.Search,
        NavigationItem.Profile
    )
    NavigationBar(
        containerColor = Purple40,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Image(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(0.4f)
                ),
                alwaysShowLabel = false,
                selected = currentRoute == item.destination.route,
                onClick = {
                    navController.navigate(item.destination.route){
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}