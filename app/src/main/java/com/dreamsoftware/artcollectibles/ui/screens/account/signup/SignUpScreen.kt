package com.dreamsoftware.artcollectibles.ui.screens.account.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.Purple500

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState(
        initialValue = SignUpUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    SignUpComponent(
        uiState = state,
        snackBarHostState = snackBarHostState,
        onEmailChanged = {
            viewModel.onEmailChanged(newEmail = it)
        },
        onPasswordChanged = {
            viewModel.onPasswordChanged(newPassword = it)
        },
        onSignUp = {
            viewModel.signUp()
        },
        onSignUpSuccess = onSignUpSuccess
    )
}

@Composable
private fun SignUpComponent(
    uiState: SignUpUiState,
    snackBarHostState: SnackbarHostState,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onSignUp: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    if (uiState.state is SignUpState.OnError) {
        val signUpFailedText = stringResource(id = R.string.signup_signup_failed)
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar(
                message = signUpFailedText
            )
        }
    }
    CommonDialog(
        isVisible = uiState.state is SignUpState.OnSuccess,
        titleRes = R.string.signup_success_dialog_title_text,
        descriptionRes = R.string.signup_success_dialog_description_text,
        acceptRes = R.string.signup_success_dialog_accept_button_text,
        onAcceptClicked = onSignUpSuccess
    )
    LoadingDialog(isShowingDialog = uiState.state is SignUpState.InProgress)
    AccountScreen(
        snackBarHostState = snackBarHostState,
        mainTitleRes = R.string.signup_main_title_text,
        screenBackgroundRes = R.drawable.common_background
    ) {
        CommonText(
            type = CommonTextTypeEnum.TITLE_LARGE,
            titleRes = R.string.onboarding_subtitle_text,
            textColor = Purple500,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(bottom = 50.dp))
        CommonDefaultTextField(
            labelRes = R.string.signup_input_email_label,
            placeHolderRes = R.string.signup_input_email_placeholder,
            keyboardType = KeyboardType.Email,
            value = uiState.email,
            onValueChanged = onEmailChanged
        )
        CommonTextFieldPassword(
            labelRes = R.string.signup_input_password_label,
            placeHolderRes = R.string.signup_input_password_placeholder,
            value = uiState.password,
            onValueChanged = onPasswordChanged
        )
        CommonButton(
            modifier = Modifier.padding(bottom = 8.dp),
            enabled = uiState.isSignUpButtonEnabled,
            text = R.string.signup_signup_button_text,
            onClick = onSignUp
        )
    }
}