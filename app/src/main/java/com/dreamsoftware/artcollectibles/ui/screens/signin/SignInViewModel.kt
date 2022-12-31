package com.dreamsoftware.artcollectibles.ui.screens.signin

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

    private val _uiState: MutableStateFlow<OnSignInUiState> = MutableStateFlow(OnSignInUiState.NoSignIn)
    val uiState: StateFlow<OnSignInUiState> = _uiState

    fun signIn(email: String, password: String) {
        signIn(SignInUseCase.AuthParams(email, password))
    }

    fun signIn(accessToken: String, authType: ExternalAuthTypeEnum) {
        signIn(SignInUseCase.ExternalAuthParams(accessToken, authType))
    }

    /**
     * Private Methods
     */

    private fun signIn(params: SignInUseCase.Params) {
        viewModelScope.launch {
            _uiState.value = OnSignInUiState.OnSignInProgress
            signInUserCase.invoke(
                params = params,
                onSuccess = {
                    _uiState.value = OnSignInUiState.OnSignInSuccess(it)
                },
                onError = {
                    _uiState.value = OnSignInUiState.OnSignInError(it)
                })
        }
    }

}

sealed interface OnSignInUiState {
    object NoSignIn : OnSignInUiState
    object OnSignInProgress : OnSignInUiState
    data class OnSignInError(val throwable: Throwable) : OnSignInUiState
    data class OnSignInSuccess(val userInfo: UserInfo) : OnSignInUiState
}