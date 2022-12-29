package com.dreamsoftware.artcollectibles.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.AuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val signInUserCase: SignInUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<OnBoardingUiState> = MutableStateFlow(OnBoardingUiState.NoSignIn)
    val uiState: StateFlow<OnBoardingUiState> = _uiState

    fun signIn(accessToken: String, authType: AuthTypeEnum) {
        viewModelScope.launch {
            _uiState.value = OnBoardingUiState.OnSignInProgress
            signInUserCase.invoke(
                params = SignInUseCase.Params(accessToken, authType),
                onSuccess = {
                    _uiState.value = OnBoardingUiState.OnSignInSuccess(it)
                },
                onError = {
                    _uiState.value = OnBoardingUiState.OnSignInError(it)
                })
        }
    }
}

sealed interface OnBoardingUiState {
    object NoSignIn : OnBoardingUiState
    object OnSignInProgress : OnBoardingUiState
    data class OnSignInError(val throwable: Throwable) : OnBoardingUiState
    data class OnSignInSuccess(val userInfo: UserInfo) : OnBoardingUiState
}