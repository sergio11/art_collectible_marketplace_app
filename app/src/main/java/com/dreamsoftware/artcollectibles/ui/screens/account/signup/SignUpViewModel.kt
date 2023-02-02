package com.dreamsoftware.artcollectibles.ui.screens.account.signup

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SignUpUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : SupportViewModel<SignUpUiState>() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    fun onEmailChanged(newEmail: String) {
        updateState {
            it.copy(
                email = newEmail,
                isSignUpButtonEnabled = signButtonUpShouldBeEnabled(newEmail, it.password.orEmpty())
            )
        }
    }

    fun onPasswordChanged(newPassword: String) {
        updateState {
            it.copy(
                password = newPassword,
                isSignUpButtonEnabled = signButtonUpShouldBeEnabled(it.email.orEmpty(), newPassword)
            )
        }
    }

    fun signUp() {
        with(uiState.value) {
            onSignUpInProgress()
            signUpUseCase.invoke(
                scope = viewModelScope,
                params = SignUpUseCase.Params(email.orEmpty(), password.orEmpty()),
                onSuccess = ::onSignUpSuccess,
                onError = ::onSignUpError
            )
        }
    }

    override fun onGetDefaultState(): SignUpUiState = SignUpUiState()

    private fun onSignUpInProgress() {
        updateState { it.copy(state = SignUpState.InProgress) }
    }

    private fun onSignUpSuccess(userInfo: UserInfo) {
        updateState { it.copy(state = SignUpState.OnSuccess(userInfo)) }
    }

    private fun onSignUpError(ex: Throwable) {
        updateState { it.copy(state = SignUpState.OnError(ex)) }
    }

    private fun signButtonUpShouldBeEnabled(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length > MIN_PASSWORD_LENGTH
}

data class SignUpUiState(
    val email: String? = null,
    val password: String? = null,
    val isSignUpButtonEnabled: Boolean = false,
    val state: SignUpState = SignUpState.Default
)

sealed class SignUpState {
    object Default : SignUpState()
    object InProgress : SignUpState()
    data class OnSuccess(val userInfo: UserInfo) : SignUpState()
    data class OnError(val error: Throwable) : SignUpState()
}
