package com.dreamsoftware.artcollectibles.ui.screens.account.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.core.BasicScreen
import com.dreamsoftware.artcollectibles.ui.components.core.CommonCardColumn
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum

@Composable
fun AccountScreen(
    snackBarHostState: SnackbarHostState,
    @StringRes mainTitleRes: Int,
    @DrawableRes screenBackgroundRes: Int,
    enableVerticalScroll: Boolean = true,
    screenContent: @Composable ColumnScope.() -> Unit
) {
    BasicScreen(
        snackBarHostState = snackBarHostState,
        titleRes = mainTitleRes,
        backgroundRes = screenBackgroundRes,
        enableVerticalScroll = enableVerticalScroll,
        hasTopBar = false,
        screenContent = {
            if(!enableVerticalScroll) {
                Spacer(modifier = Modifier.weight(1f))
            }
            CommonText(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                type = CommonTextTypeEnum.HEADLINE_LARGE,
                textColor = Color.White,
                titleRes = mainTitleRes,
                textBold = true
            )
            if(!enableVerticalScroll) {
                Spacer(modifier = Modifier.weight(1f))
            }
            CommonCardColumn(content = screenContent)
        }
    )
}