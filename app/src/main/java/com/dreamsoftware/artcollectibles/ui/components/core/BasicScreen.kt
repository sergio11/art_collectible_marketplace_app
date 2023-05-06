package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.BottomBar
import com.dreamsoftware.artcollectibles.ui.components.ScreenBackgroundImage
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.whiteTranslucent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreen(
    snackBarHostState: SnackbarHostState,
    @StringRes titleRes: Int? = null,
    @DrawableRes backgroundRes: Int? = R.drawable.screen_background_2,
    titleText: String? = null,
    centerTitle: Boolean = false,
    menuActions: List<TopBarAction> = emptyList(),
    navigationAction: TopBarAction? = null,
    navController: NavController? = null,
    screenContainerColor: Color = MaterialTheme.colorScheme.background,
    hasBottomBar: Boolean = false,
    hasTopBar: Boolean = true,
    enableVerticalScroll: Boolean = false,
    errorMessage: String? = null,
    onBuildCustomTopBar: @Composable (() -> Unit)? = null,
    screenContent: @Composable ColumnScope.() -> Unit = {},
    backgroundContent: @Composable BoxScope.() -> Unit = {}
) {
    if(!errorMessage.isNullOrBlank()) {
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }
    Scaffold(
        bottomBar = {
            navController?.let {
                if(hasBottomBar) {
                    BottomBar(navController = it)
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                Snackbar(
                    shape = RoundedCornerShape(10.dp),
                    containerColor = Purple200,
                    actionColor = Purple500,
                    contentColor = Color.White,
                    snackbarData = data
                )
            }
        },
        topBar = {
            if(hasTopBar) {
                onBuildCustomTopBar?.invoke() ?: CommonTopAppBar(
                    titleRes = titleRes,
                    titleText = titleText,
                    navigationAction = navigationAction,
                    centerTitle = centerTitle,
                    menuActions =  menuActions
                )
            }
        },
        containerColor = screenContainerColor
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            backgroundRes?.let {
                ScreenBackgroundImage(imageRes = it)
            }
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