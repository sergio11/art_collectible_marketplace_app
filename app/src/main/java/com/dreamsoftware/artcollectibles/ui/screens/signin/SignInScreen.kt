package com.dreamsoftware.artcollectibles.ui.screens.signin

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
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
import com.dreamsoftware.artcollectibles.domain.models.ExternalAuthTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState<SignInUiState>(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInComponent(
    modifier: Modifier = Modifier,
    state: SignInUiState,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onSignIn: () -> Unit,
    onSocialSignIn: (token: String, authType: ExternalAuthTypeEnum) -> Unit
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
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ScreenBackgroundImage()
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(top = 80.dp)
                    .fillMaxSize()
            ) {
                Text(
                    stringResource(R.string.signin_main_title_text),
                    color = Color.White,
                    fontSize = 37.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.weight(1f))
                Card(
                    modifier = Modifier.padding(bottom = 20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(27.dp),
                    border = BorderStroke(3.dp, Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(27.dp)
                    ) {
                        Text(
                            stringResource(R.string.onboarding_subtitle_text),
                            color = Purple500,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(bottom = 50.dp))
                        CommonTextField(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            placeHolderRes = R.string.signin_input_email_placeholder,
                            keyboardType = KeyboardType.Email,
                            value = state.email,
                            onValueChanged = onEmailChanged
                        )
                        Spacer(modifier = Modifier.padding(bottom = 30.dp))
                        CommonTextField(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            placeHolderRes = R.string.signin_input_password_placeholder,
                            keyboardType = KeyboardType.Password,
                            value = state.password,
                            onValueChanged = onPasswordChanged
                        )
                        Spacer(modifier = Modifier.padding(bottom = 30.dp))
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
                                onSocialSignIn(it, ExternalAuthTypeEnum.FACEBOOK)
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
                                onSocialSignIn(it, ExternalAuthTypeEnum.GOOGLE)
                                Log.d("GOOGLE_LOGIN", "onAuthSuccess $it CALLED!")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ScreenBackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.onboarding_bg_1),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
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