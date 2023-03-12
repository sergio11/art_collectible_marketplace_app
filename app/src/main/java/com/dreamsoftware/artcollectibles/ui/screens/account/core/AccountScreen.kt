package com.dreamsoftware.artcollectibles.ui.screens.account.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamsoftware.artcollectibles.ui.components.ScreenBackgroundImage
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    @StringRes mainTitleRes: Int,
    @DrawableRes screenBackgroundRes: Int,
    screenContent: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage(screenBackgroundRes)
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp)
                    .padding(top = 80.dp)
                    .fillMaxSize()
            ) {
                Text(
                    stringResource(mainTitleRes),
                    color = Color.White,
                    fontSize = 37.sp,
                    lineHeight = 40.sp,
                    fontFamily = montserratFontFamily,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(60.dp))
                Card(
                    modifier = Modifier.padding(bottom = 20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(27.dp),
                    border = BorderStroke(3.dp, Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(27.dp),
                        content = screenContent
                    )
                }
            }
        }
    }
}