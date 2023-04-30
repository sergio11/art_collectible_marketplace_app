package com.dreamsoftware.artcollectibles.ui.screens.preferences

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.BuildConfig
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple200

@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = PreferencesUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {

        }
        PreferencesComponent(
            state = uiState,
            snackBarHostState = snackBarHostState,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
private fun PreferencesComponent(
    state: PreferencesUiState,
    snackBarHostState: SnackbarHostState,
    onBackPressed: () -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        BasicScreen(
            snackBarHostState = snackBarHostState,
            titleRes = R.string.preferences_screen_main_title_text,
            centerTitle = true,
            enableVerticalScroll = true,
            navigationAction = TopBarAction(
                iconRes = R.drawable.back_icon,
                onActionClicked = onBackPressed
            ),
            screenContent = {
                CommonCardColumn {
                    val defaultModifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(300.dp)
                    CommonText(
                        modifier = defaultModifier,
                        type = CommonTextTypeEnum.TITLE_LARGE,
                        titleRes = R.string.preferences_screen_subtitle_text,
                        textColor = DarkPurple,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    PreferenceItem(
                        titleRes = R.string.preferences_screen_profile_visibility_title,
                        descriptionRes = R.string.preferences_screen_profile_visibility_description
                    )
                    PreferenceItem(
                        titleRes = R.string.preferences_screen_account_balance_title,
                        descriptionRes = R.string.preferences_screen_account_balance_description
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    CommonText(
                        modifier = defaultModifier,
                        type = CommonTextTypeEnum.BODY_MEDIUM,
                        titleRes = R.string.preferences_screen_bottom_text,
                        textColor = DarkPurple,
                        textAlign = TextAlign.Center
                    )
                    CommonText(
                        modifier = defaultModifier,
                        type = CommonTextTypeEnum.BODY_SMALL,
                        titleText = "Version Name: ${BuildConfig.VERSION_NAME} | Version Code: ${BuildConfig.VERSION_CODE}",
                        textColor = DarkPurple,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        )
    }
}

@Composable
private fun PreferenceItem(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    checked: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .width(320.dp)
            .height(80.dp)
            .border(2.dp, Purple200, RoundedCornerShape(percent = 30))
            .background(Color.White, RoundedCornerShape(percent = 30)),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(8.dp).weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                CommonText(
                    type = CommonTextTypeEnum.TITLE_MEDIUM,
                    titleRes = titleRes,
                    textColor = DarkPurple,
                    singleLine = true
                )
                CommonText(
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    titleRes = descriptionRes,
                    textColor = Purple200,
                    maxLines = 2
                )
            }
            Switch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(

                )
            )
        }
    }
}