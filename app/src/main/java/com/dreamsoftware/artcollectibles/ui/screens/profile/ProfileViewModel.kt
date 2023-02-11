package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CloseSessionUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCurrentBalanceUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAuthUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UpdateUserInfoUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Profile View Model
 * @param applicationAware
 * @param getAuthUserProfileUseCase
 * @param getCurrentBalanceUseCase
 * @param updateUserInfoUseCase
 * @param closeSessionUseCase
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val applicationAware: IApplicationAware,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val getCurrentBalanceUseCase: GetCurrentBalanceUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val closeSessionUseCase: CloseSessionUseCase
) : SupportViewModel<ProfileUiState>() {

    override fun onGetDefaultState(): ProfileUiState = ProfileUiState()

    fun isProfileLoaded() = uiState.value.userInfo != null

    fun onInfoChanged(newInfo: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(info = newInfo))
            }
        }
    }

    fun onPictureChanged(imageUri: Uri) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(
                    userInfo = it.userInfo?.copy(photoUrl = imageUri.toString()),
                    isProfilePictureUpdated = true
                )
            }
        }
    }

    fun onBirthdateChanged(newBirthdate: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(birthdate = newBirthdate))
            }
        }
    }

    fun onNameChanged(newName: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(name = newName))
            }
        }
    }

    fun onProfessionalTitleChanged(newProfessionalTitle: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(professionalTitle = newProfessionalTitle))
            }
        }
    }

    fun load() {
        onLoading()
        loadProfileData()
    }

    fun getFileProviderAuthority() = applicationAware.getFileProviderAuthority()

    fun saveUserInfo() {
        with(uiState.value) {
            userInfo?.let {
                onLoading()
                updateUserInfoUseCase.invoke(
                    scope = viewModelScope,
                    params = UpdateUserInfoUseCase.Params(
                        userInfo = it,
                        isProfilePictureUpdated = isProfilePictureUpdated
                    ),
                    onSuccess = ::onProfileUpdated,
                    onError = ::onErrorOccurred
                )
            }
        }
    }

    fun closeSession() {
        onLoading()
        closeSessionUseCase.invoke(
            scope = viewModelScope,
            onSuccess = {
                onSessionClosed()
            }, onError = ::onErrorOccurred
        )
    }

    /**
     * Private Methods
     */

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                val userProfile = getAuthUserProfileUseCase.invoke(scope = viewModelScope)
                val currentBalance = getCurrentBalanceUseCase.invoke(scope = viewModelScope)
                onLoadProfileDataCompleted(userProfile, currentBalance)
            } catch (ex: Exception) {
                ex.printStackTrace()
                onErrorOccurred(ex)
            }
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onLoadProfileDataCompleted(userInfo: UserInfo, currentBalance: AccountBalance) {
        updateState {
            it.copy(
                userInfo = userInfo,
                isLoading = false,
                isProfilePictureUpdated = false,
                accountBalance = currentBalance
            )
        }
    }

    private fun onProfileUpdated(userInfo: UserInfo) {
        updateState {
            it.copy(
                userInfo = userInfo,
                isLoading = false,
                isProfilePictureUpdated = false,
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState { it.copy(isLoading = false) }
    }

    private fun onSessionClosed() {
        updateState { it.copy(isLoading = false, isSessionClosed = true) }
    }
}

data class ProfileUiState(
    val userInfo: UserInfo? = null,
    val accountBalance: AccountBalance? = null,
    val isProfilePictureUpdated: Boolean = false,
    val isLoading: Boolean = false,
    val isSessionClosed: Boolean = false,
)