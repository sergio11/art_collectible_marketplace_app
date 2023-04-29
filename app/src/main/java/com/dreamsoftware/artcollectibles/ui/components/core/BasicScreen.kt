package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.BottomBar
import com.dreamsoftware.artcollectibles.ui.components.ScreenBackgroundImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreen(
    snackBarHostState: SnackbarHostState,
    @StringRes titleRes: Int? = null,
    @DrawableRes backgroundRes: Int = R.drawable.screen_background_2,
    titleText: String? = null,
    centerTitle: Boolean = false,
    menuActions: List<TopBarAction> = emptyList(),
    navigationAction: TopBarAction? = null,
    navController: NavController? = null,
    screenContainerColor: Color = MaterialTheme.colorScheme.background,
    hasBottomBar: Boolean = false,
    enableVerticalScroll: Boolean = false,
    screenContent: @Composable ColumnScope.() -> Unit = {},
    backgroundContent: @Composable BoxScope.() -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            navController?.let {
                if(hasBottomBar) {
                    BottomBar(navController = it)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CommonTopAppBar(
                titleRes = titleRes,
                titleText = titleText,
                navigationAction = navigationAction,
                centerTitle = centerTitle,
                menuActions =  menuActions
            )
        },
        containerColor = screenContainerColor
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage(imageRes = backgroundRes)
            Column(modifier = if(enableVerticalScroll) {
                Modifier.verticalScroll(rememberScrollState())
            } else {
                Modifier.fillMaxWidth()
            }) {
                screenContent()
            }
            backgroundContent()
        }
    }
}