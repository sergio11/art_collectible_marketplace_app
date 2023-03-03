package com.dreamsoftware.artcollectibles.ui.screens.followers

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetFollowersUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetFollowingUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Followers View Model
 * @param getFollowersUseCase
 * @param getFollowingUseCase
 */
@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingUseCase: GetFollowingUseCase
) : SupportViewModel<FollowersUiState>() {

    override fun onGetDefaultState(): FollowersUiState = FollowersUiState()

    fun loadFollowers(userUid: String) {
        onLoading()
        getFollowersUseCase.invoke(
            scope = viewModelScope,
            params = GetFollowersUseCase.Params(userUid),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    fun loadFollowing(userUid: String) {
        onLoading()
        getFollowingUseCase.invoke(
            scope = viewModelScope,
            params = GetFollowingUseCase.Params(userUid),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(userResult: Iterable<UserInfo>) {
        updateState {
            it.copy(
                isLoading = false,
                userResult = userResult
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
        updateState {
            it.copy(
                isLoading = false,
                error = ex
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }
}

data class FollowersUiState(
    val isLoading: Boolean = false,
    val userResult: Iterable<UserInfo> = emptyList(),
    var error: Exception? = null
)