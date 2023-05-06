package com.dreamsoftware.artcollectibles.ui.screens.preferences

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.models.UserPreferences
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAuthUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UpdateUserInfoUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val preferencesScreenErrorMapper: PreferencesScreenErrorMapper
): SupportViewModel<PreferencesUiState>() {

    override fun onGetDefaultState(): PreferencesUiState = PreferencesUiState()

    private var authUserInfo: UserInfo? = null

    fun load() {
        onLoading()
        getAuthUserProfileUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    fun onIsPublicProfilePreferenceChanged(value: Boolean) {
        updatePreference {
            it?.copy(isPublicProfile = value)
        }
    }

    fun onShowAccountBalancePreferenceChanged(value: Boolean) {
        updatePreference {
            it?.copy(showAccountBalance = value)
        }
    }

    fun onShowSellingTokensRowPreferenceChanged(value: Boolean) {
        updatePreference {
            it?.copy(showSellingTokensRow = value)
        }
    }

    fun onShowLastTransactionsOfTokensPreferenceChanged(value: Boolean) {
        updatePreference {
            it?.copy(showLastTransactionsOfTokens = value)
        }
    }

    fun onAllowPublishCommentsPreferenceChanged(value: Boolean) {
        updatePreference {
            it?.copy(allowPublishComments = value)
        }
    }

    fun saveData() {
        authUserInfo?.let { userInfo ->
            onLoading()
            with(uiState.value) {
                updateUserInfoUseCase.invoke(
                    scope = viewModelScope,
                    params = UpdateUserInfoUseCase.Params(
                        userInfo = userInfo.copy(
                            preferences = userPreferences
                        )
                    ),
                    onSuccess = ::onSuccess,
                    onError = ::onErrorOccurred
                )
            }
        }
    }

    private fun updatePreference(reducer: (currentPreferences: UserPreferences?) -> UserPreferences?) {
        updateState {
            it.copy(
                userPreferences = it.userPreferences?.let(reducer)
            )
        }
    }

    private fun onSuccess(userInfo: UserInfo) {
        authUserInfo = userInfo
        updateState {
            it.copy(
                isLoading = false,
                userPreferences = userInfo.preferences,
                errorMessage = null
            )
        }
    }

    private fun onErrorOccurred(ex: Throwable) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = preferencesScreenErrorMapper.mapToMessage(ex)
            )
        }
    }

    private fun onLoading() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
    }

}

data class PreferencesUiState(
    val isLoading: Boolean = false,
    val userPreferences: UserPreferences? = null,
    val errorMessage: String? = null
)