package com.dreamsoftware.artcollectibles.ui.screens.account.onboarding

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onUserAlreadyAuthenticated: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {}
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<OnBoardingUiState>(
        initialValue = OnBoardingUiState.NoAuthenticated,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                Log.d("ONBOARDING", "OnBoardingScreen state.value -> $it CALLED!")
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
        Log.d("ONBOARDING", "viewModel.verifyUserSession() CALLED!")
        viewModel.verifyUserSession()
    }
    OnBoardingComponent(
        modifier = modifier,
        state = uiState,
        snackBarHostState = snackBarHostState,
        onLoginClicked = onNavigateToLogin,
        onSignUpClicked = onNavigateToSignUp
    )
}


@Composable
internal fun OnBoardingComponent(
    modifier: Modifier = Modifier,
    state: OnBoardingUiState,
    snackBarHostState: SnackbarHostState,
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    LoadingDialog(isShowingDialog = state is OnBoardingUiState.VerificationInProgress)
    AccountScreen(
        modifier = modifier,
        snackBarHostState = snackBarHostState,
        mainTitleRes = R.string.onboarding_main_title_text,
        screenBackgroundRes = R.drawable.common_background
    ) {
        Text(
            stringResource(R.string.onboarding_subtitle_text),
            color = Purple500,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        Text(
            stringResource(R.string.onboarding_description_text),
            color = Purple700,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(bottom = 20.dp))
        CommonButton(
            modifier = Modifier.padding(bottom = 4.dp),
            text = R.string.onboarding_login_button_text,
            onClick = onLoginClicked
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        CommonButton(
            modifier = Modifier.padding(bottom = 4.dp),
            text = R.string.onboarding_signup_button_text,
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple700,
                contentColor = Color.White
            ),
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