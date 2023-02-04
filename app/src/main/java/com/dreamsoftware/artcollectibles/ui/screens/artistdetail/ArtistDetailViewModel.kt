package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetUserProfileUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : SupportViewModel<ArtistDetailUiState>() {

    override fun onGetDefaultState(): ArtistDetailUiState = ArtistDetailUiState()

    fun loadDetail(uid: String) {
        onLoading()
        loadAllDataForUser(uid)
    }

    private fun loadAllDataForUser(uid: String) {
        viewModelScope.launch {
            try {
                val userInfo = getUserProfileUseCase.invoke(
                    scope = this,
                    params = GetUserProfileUseCase.Params(uid)
                )
                onLoadAllDataCompleted(userInfo)
            } catch (ex: Exception) {
                onErrorOccurred(ex)
            }
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onLoadAllDataCompleted(userInfo: UserInfo) {
        updateState {
            it.copy(
                isLoading = false,
                userInfo = userInfo
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(isLoading = false)
        }
    }
}

data class ArtistDetailUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null
)