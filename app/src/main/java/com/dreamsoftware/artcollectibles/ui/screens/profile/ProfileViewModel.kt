package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CloseSessionUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCurrentBalanceUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UpdateUserInfoUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Profile View Model
 * @param applicationAware
 * @param getUserProfileUseCase
 * @param getCurrentBalanceUseCase
 * @param updateUserInfoUseCase
 * @param closeSessionUseCase
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val applicationAware: IApplicationAware,
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

    fun onPictureChanged(imageUri: Uri) {
        if(isProfileLoaded()) {
            Log.d("ART_COLL", "imageUri.toString() -> $imageUri CALLED!")
            updateState {
                it.copy(userInfo = it.userInfo?.copy(photoUrl = imageUri.toString()))
            }
        }
    }

    fun onBirthdateChanged(newBirthdate: String) {
        if(isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(birthdate = newBirthdate))
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

    fun getFileProviderAuthority() =
        applicationAware.getFileProviderAuthority()

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
                onSuccess = { balance ->
                    updateState {
                        it.copy(accountBalance = balance)
                    }
                },
                onError = {
                    updateState {
                        it.copy(accountBalance = null)
                    }
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
    val accountBalance: AccountBalance? = null,
    val isLoading: Boolean = false,
    val isSessionClosed: Boolean = false,
)