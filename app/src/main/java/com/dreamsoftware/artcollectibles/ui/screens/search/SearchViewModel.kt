package com.dreamsoftware.artcollectibles.ui.screens.search

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FindAllUsersUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search View Model
 * @param findAllUsersUseCase
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val findAllUsersUseCase: FindAllUsersUseCase
) : SupportViewModel<SearchUiState>() {

    override fun onGetDefaultState(): SearchUiState = SearchUiState()

    fun load() {
        onLoading()
        searchUsers()
    }

    private fun searchUsers() {
        viewModelScope.launch {
            findAllUsersUseCase.invoke(
                onSuccess = ::onSearchFinished,
                onError = ::onErrorOccurred
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onSearchFinished(userResult: Iterable<UserInfo>) {
        updateState {
            it.copy(
                userResult = userResult,
                isLoading = false
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {

    }
}

data class SearchUiState(
    val userResult: Iterable<UserInfo> = emptyList(),
    val isLoading: Boolean = false,
)