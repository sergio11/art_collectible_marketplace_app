package com.dreamsoftware.artcollectibles.ui.screens.account.onboarding

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.usecase.impl.RestoreUserAuthenticatedSessionUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val restoreUserAuthenticatedSessionUseCase: RestoreUserAuthenticatedSessionUseCase
) : SupportViewModel<OnBoardingUiState>() {

    fun restoreUserSession() {
        updateState { OnBoardingUiState.VerificationInProgress }
        restoreUserAuthenticatedSessionUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { isAuthenticated ->
                updateState {
                    if (isAuthenticated) {
                        OnBoardingUiState.UserAlreadyAuthenticated
                    } else {
                        OnBoardingUiState.NoAuthenticated
                    }
                }
            },
            onError = {
                it.printStackTrace()
                updateState { OnBoardingUiState.NoAuthenticated }
            }
        )
    }

    override fun onGetDefaultState(): OnBoardingUiState = OnBoardingUiState.NoAuthenticated

}

sealed class OnBoardingUiState {
    object NoAuthenticated : OnBoardingUiState()
    object VerificationInProgress : OnBoardingUiState()
    object UserAlreadyAuthenticated : OnBoardingUiState()
}