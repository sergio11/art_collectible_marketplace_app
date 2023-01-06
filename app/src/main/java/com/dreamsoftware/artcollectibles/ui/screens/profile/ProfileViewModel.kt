package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CloseSessionUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCurrentBalanceUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UpdateUserInfoUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

/**
 * Profile View Model
 * @param getUserProfileUseCase
 * @param getCurrentBalanceUseCase
 * @param updateUserInfoUseCase
 * @param closeSessionUseCase
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getCurrentBalanceUseCase: GetCurrentBalanceUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val closeSessionUseCase: CloseSessionUseCase
): SupportViewModel<ProfileUiState>() {

    override fun onGetDefaultState(): ProfileUiState = ProfileUiState()

    fun isProfileLoaded() =
        uiState.value.userInfo != null

    fun onInfoChanged(newInfo: String) {
        if(isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(info = newInfo))
            }
        }
    }

    fun onNameChanged(newName: String) {
        if(isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(name = newName))
            }
        }
    }

    fun load() {
        onLoading()
        loadProfileData()
        loadCurrentBalance()
    }

    fun saveUserInfo() {
        uiState.value.userInfo?.let {
            viewModelScope.launch {
                onLoading()
                updateUserInfoUseCase.invoke(
                    params = UpdateUserInfoUseCase.Params(it),
                    onSuccess = ::onProfileLoaded,
                    onError = ::onErrorOccurred
                )
            }
        }
    }

    fun closeSession() {
        viewModelScope.launch {
            onLoading()
            closeSessionUseCase.invoke(onSuccess = {
                onSessionClosed()
            }, onError = ::onErrorOccurred)
        }
    }

    /**
     * Private Methods
     */

    private fun loadProfileData() {
        viewModelScope.launch {
            getUserProfileUseCase.invoke(
                onSuccess = ::onProfileLoaded,
                onError = ::onErrorOccurred
            )
        }
    }

    private fun loadCurrentBalance() {
        viewModelScope.launch {
            getCurrentBalanceUseCase.invoke(
                onSuccess = {
                    Log.d("ART_COLL", "loadCurrentBalance - onSuccess - $it")
                },
                onError = {
                    Log.d("ART_COLL", "loadCurrentBalance - onError - $it")
                }
            )
        }
    }

    private fun onLoading(){
        updateState { it.copy(isLoading = true) }
    }

    private fun onProfileLoaded(userInfo: UserInfo) {
        updateState { it.copy(userInfo = userInfo, isLoading = false) }
    }

    private fun onErrorOccurred(ex: Exception) {

    }

    private fun onSessionClosed() {
        updateState { it.copy(isLoading = false, isSessionClosed = true) }
    }
}

data class ProfileUiState(
    val userInfo: UserInfo? = null,
    val accountCurrentBalance: BigInteger? = null,
    val isLoading: Boolean = false,
    val isSessionClosed: Boolean = false,
)