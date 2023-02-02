package com.dreamsoftware.artcollectibles.ui.screens.account.onboarding

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.usecase.impl.VerifyUserAuthenticatedUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val verifyUserAuthenticatedUseCase: VerifyUserAuthenticatedUseCase
) : SupportViewModel<OnBoardingUiState>() {

    fun verifyUserSession() {
        updateState { OnBoardingUiState.VerificationInProgress }
        verifyUserAuthenticatedUseCase.invoke(
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