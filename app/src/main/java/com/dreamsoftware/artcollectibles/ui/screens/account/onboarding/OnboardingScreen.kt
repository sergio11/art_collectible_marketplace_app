package com.dreamsoftware.artcollectibles.ui.screens.account.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onUserAlreadyAuthenticated: () -> Unit = {},
    onGoToLogin: () -> Unit = {},
    onGoToSignUp: () -> Unit = {}
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<OnBoardingUiState>(
        initialValue = OnBoardingUiState.NoAuthenticated,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if(it is OnBoardingUiState.UserAlreadyAuthenticated) {
                    onUserAlreadyAuthenticated()
                } else {
                    value = it
                }
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
        viewModel.restoreUserSession()
    }
    OnBoardingComponent(
        state = uiState,
        snackBarHostState = snackBarHostState,
        onLoginClicked = onGoToLogin,
        onSignUpClicked = onGoToSignUp
    )
}


@Composable
private fun OnBoardingComponent(
    state: OnBoardingUiState,
    snackBarHostState: SnackbarHostState,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    LoadingDialog(isShowingDialog = state is OnBoardingUiState.VerificationInProgress)
    AccountScreen(
        snackBarHostState = snackBarHostState,
        mainTitleRes = R.string.onboarding_main_title_text,
        screenBackgroundRes = R.drawable.common_background,
        enableVerticalScroll = false
    ) {
        CommonText(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            type = CommonTextTypeEnum.TITLE_LARGE,
            textColor = Purple500,
            titleRes = R.string.onboarding_subtitle_text,
            textAlign = TextAlign.Center,
            textBold = true
        )
        CommonText(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            type = CommonTextTypeEnum.TITLE_MEDIUM,
            textColor = Purple700,
            titleRes = R.string.onboarding_description_text,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
        CommonButton(
            text = R.string.onboarding_login_button_text,
            onClick = onLoginClicked
        )
        CommonButton(
            text = R.string.onboarding_signup_button_text,
            containerColor = Purple40,
            onClick = onSignUpClicked
        )
    }
}


@Composable
@Preview
fun PreviewOnBoardingScreen() {
    ArtCollectibleMarketplaceTheme {
        OnBoardingScreen {
            //Navigate to the next screen
        }
    }
}