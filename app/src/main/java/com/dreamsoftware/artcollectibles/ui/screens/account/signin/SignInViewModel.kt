package com.dreamsoftware.artcollectibles.ui.screens.account.signin

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SignInUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SocialSignInUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUserCase: SignInUseCase,
    private val socialSignInUseCase: SocialSignInUseCase
) : SupportViewModel<SignInUiState>() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    fun onEmailChanged(newEmail: String) {
        updateState {
            it.copy(
                email = newEmail,
                isLoginButtonEnabled = loginButtonShouldBeEnabled(newEmail, it.password.orEmpty())
            )
        }
    }

    fun onPasswordChanged(newPassword: String) {
        updateState {
            it.copy(
                password = newPassword,
                isLoginButtonEnabled = loginButtonShouldBeEnabled(it.email.orEmpty(), newPassword)
            )
        }
    }

    fun signIn() {
        with(uiState.value) {
            onLoginInProgress()
            signInUserCase.invoke(
                scope = viewModelScope,
                params = SignInUseCase.Params(email.orEmpty(), password.orEmpty()),
                onSuccess = ::onLoginSuccess,
                onError = ::onLoginError
            )
        }
    }

    fun signIn(accessToken: String, authType: ExternalProviderAuthTypeEnum) {
        onLoginInProgress()
        socialSignInUseCase.invoke(
            scope = viewModelScope,
            params = SocialSignInUseCase.Params(accessToken, authType),
            onSuccess = ::onLoginSuccess,
            onError = ::onLoginError
        )
    }

    override fun onGetDefaultState(): SignInUiState = SignInUiState()

    /**
     * Private Methods
     */

    private fun onLoginSuccess(userInfo: UserInfo) {
        updateState { it.copy(loginState = LoginState.OnLoginSuccess(userInfo)) }
    }

    private fun onLoginError(ex: Throwable) {
        updateState { it.copy(loginState = LoginState.OnLoginError(ex)) }
    }

    private fun onLoginInProgress() {
        updateState { it.copy(loginState = LoginState.OnLoginInProgress) }
    }

    private fun loginButtonShouldBeEnabled(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length > MIN_PASSWORD_LENGTH

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