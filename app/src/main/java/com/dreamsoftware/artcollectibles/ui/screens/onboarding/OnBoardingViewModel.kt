package com.dreamsoftware.artcollectibles.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val signInUserCase: SignInUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<OnBoardingUiState> = MutableStateFlow(OnBoardingUiState.NoSignIn)
    val uiState: StateFlow<OnBoardingUiState> = _uiState


}

sealed interface OnBoardingUiState {
    object NoSignIn : OnBoardingUiState
}