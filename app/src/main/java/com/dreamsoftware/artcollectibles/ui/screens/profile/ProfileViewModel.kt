package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CloseSessionUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetUserProfileUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Profile View Model
 * @param getUserProfileUseCase
 * @param closeSessionUseCase
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val closeSessionUseCase: CloseSessionUseCase
): SupportViewModel<ProfileUiState>() {

    override fun onGetDefaultState(): ProfileUiState = ProfileUiState()

    fun isProfileLoaded() =
        uiState.value.userInfo != null

    fun loadProfile() {
        Log.d("PROFILE_VM", "loadProfile CALLED!")
        viewModelScope.launch {
            onLoading()
            getUserProfileUseCase.invoke(
                onSuccess = ::onProfileLoaded,
                onError = ::onErrorOccurred
            )
        }
    }

    fun closeSession() {
        viewModelScope.launch {
            onLoading()
            closeSessionUseCase.invoke(onSuccess = {

            }, onError = {

            })
        }
    }

    /**
     * Private Methods
     */

    private fun onLoading(){
        updateState { it.copy(isLoading = true) }
    }

    private fun onProfileLoaded(userInfo: UserInfo) {
        updateState { it.copy(userInfo = userInfo, isLoading = false) }
    }

    private fun onErrorOccurred(ex: Exception) {

    }
}

data class ProfileUiState(
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = false
)