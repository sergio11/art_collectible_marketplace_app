package com.dreamsoftware.artcollectibles.ui.screens.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ExternalAuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUserCase: SignInUseCase
) : ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _uiState: MutableStateFlow<SignInUiState> =
        MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState

    fun onEmailChanged(newEmail: String) {
        updateState {
            it.copy(
                email = newEmail,
                isLoginButtonEnabled = loginButtonShouldBeEnabled()
            )
        }
    }

    fun onPasswordChanged(newPassword: String) {
        updateState {
            it.copy(
                password = newPassword,
                isLoginButtonEnabled = loginButtonShouldBeEnabled()
            )
        }
    }

    fun signIn() {
        with(_uiState.value) {
            signIn(SignInUseCase.AuthParams(email.orEmpty(), password.orEmpty()))
        }
    }

    fun signIn(accessToken: String, authType: ExternalAuthTypeEnum) {
        signIn(SignInUseCase.ExternalAuthParams(accessToken, authType))
    }

    /**
     * Private Methods
     */

    private fun updateState(reducer: (currentState: SignInUiState) -> SignInUiState) {
        _uiState.value = reducer(_uiState.value)
    }

    private fun loginButtonShouldBeEnabled() = with(_uiState.value) {
        Patterns.EMAIL_ADDRESS.matcher(email.orEmpty()).matches()
                && password.orEmpty().length > MIN_PASSWORD_LENGTH
    }

    private fun signIn(params: SignInUseCase.Params) {
        viewModelScope.launch {
            updateState { it.copy(loginState = LoginState.OnLoginInProgress) }
            signInUserCase.invoke(
                params = params,
                onSuccess = { userInfo ->
                    updateState { it.copy(loginState = LoginState.OnLoginSuccess(userInfo)) }
                },
                onError = { ex ->
                    updateState { it.copy(loginState = LoginState.OnLoginError(ex)) }
                })
        }
    }

}

data class SignInUiState(
    val email: String? = null,
    val password: String? = null,
    val isLoginButtonEnabled: Boolean = false,
    val loginState: LoginState = LoginState.NoLogin
)

sealed class LoginState {
    object NoLogin : LoginState()
    object OnLoginInProgress : LoginState()
    data class OnLoginSuccess(val userInfo: UserInfo) : LoginState()
    data class OnLoginError(val error: Throwable) : LoginState()
}