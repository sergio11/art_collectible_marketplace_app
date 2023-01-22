package com.dreamsoftware.artcollectibles.ui.screens.account.signin

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState(
        initialValue = SignInUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if (it.loginState is LoginState.OnLoginSuccess) {
                    onSignInSuccess()
                } else {
                    value = it
                }
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    SignInComponent(
        modifier,
        state,
        snackBarHostState,
        coroutineScope,
        onEmailChanged = {
            viewModel.onEmailChanged(newEmail = it)
        },
        onPasswordChanged = {
            viewModel.onPasswordChanged(newPassword = it)
        },
        onSignIn = {
            viewModel.signIn()
        },
        onSocialSignIn = { token, authType ->
            viewModel.signIn(token, authType)
        }
    )
}

@Composable
internal fun SignInComponent(
    modifier: Modifier = Modifier,
    state: SignInUiState,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onSignIn: () -> Unit,
    onSocialSignIn: (token: String, authType: ExternalProviderAuthTypeEnum) -> Unit
) {
    Log.d("SIGN_IN", "SignInComponent state -> $state")
    if (state.loginState is LoginState.OnLoginError) {
        val loginFailedText = stringResource(id = R.string.signin_login_failed)
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar(
                message = loginFailedText
            )
        }
    }
    LoadingDialog(isShowingDialog = state.loginState is LoginState.OnLoginInProgress)
    AccountScreen(
        modifier = modifier,
        snackBarHostState = snackBarHostState,
        mainTitleRes = R.string.signin_main_title_text,
        screenBackgroundRes = R.drawable.common_background) {
        Text(
            stringResource(R.string.onboarding_subtitle_text),
            color = Purple500,
            fontWeight = FontWeight.Bold,
            fontFamily = montserratFontFamily,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(bottom = 50.dp))
        CommonDefaultTextField(
            labelRes = R.string.signin_input_email_label,
            placeHolderRes = R.string.signin_input_email_placeholder,
            keyboardType = KeyboardType.Email,
            value = state.email,
            onValueChanged = onEmailChanged
        )
        CommonTextFieldPassword(
            labelRes = R.string.signin_input_password_label,
            placeHolderRes = R.string.signin_input_password_placeholder,
            value = state.password,
            onValueChanged = onPasswordChanged
        )
        CommonButton(
            modifier = Modifier.padding(bottom = 8.dp),
            enabled = state.isLoginButtonEnabled,
            text = R.string.signin_login_button_text,
            onClick = onSignIn
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        AlternativeLoginDivider()
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        val loginFBCancelledText =
            stringResource(id = R.string.signin_login_facebook_cancelled)
        val loginFBFailedText =
            stringResource(id = R.string.signin_login_facebook_failed)
        FacebookLoginButton(
            enabled = state.loginState !is LoginState.OnLoginInProgress,
            modifier = Modifier.padding(horizontal = 20.dp),
            onSuccess = {
                onSocialSignIn(it, ExternalProviderAuthTypeEnum.FACEBOOK)
            },
            onCancel = {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = loginFBCancelledText
                    )
                }
            },
            onError = {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = loginFBFailedText
                    )
                }
            }
        )
        val loginGGFailedText =
            stringResource(id = R.string.signin_login_google_failed)
        GoogleLoginButton(
            enabled = state.loginState !is LoginState.OnLoginInProgress,
            modifier = Modifier.padding(horizontal = 20.dp),
            onAuthFailed = {
                Log.d("GOOGLE_LOGIN", "onAuthFailed CALLED!")
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = loginGGFailedText
                    )
                }
            },
            onAuthSuccess = {
                onSocialSignIn(it, ExternalProviderAuthTypeEnum.GOOGLE)
                Log.d("GOOGLE_LOGIN", "onAuthSuccess $it CALLED!")
            }
        )
    }
}

@Composable
internal fun AlternativeLoginDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.signin_alternative_login_text),
            fontFamily = montserratFontFamily,
            color = Color.Gray
        )
        Divider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}


@Composable
@Preview
fun PreviewOnBoardingScreen() {
    ArtCollectibleMarketplaceTheme {
        SignInScreen {
            //Navigate to the next screen
        }
    }
}