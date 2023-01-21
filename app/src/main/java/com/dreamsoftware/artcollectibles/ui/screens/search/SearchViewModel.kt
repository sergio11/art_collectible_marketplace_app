package com.dreamsoftware.artcollectibles.ui.screens.search

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SearchUsersUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search View Model
 * @param searchUsersUseCase
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : SupportViewModel<SearchUiState>() {

    override fun onGetDefaultState(): SearchUiState = SearchUiState()

    fun load() {
        onLoading()
        searchUsers()
    }

    fun onTermChanged(newTerm: String) {
        updateState { it.copy(searchTerm = newTerm) }
        searchUsers()
    }

    fun onResetSearch() {
        updateState { it.copy(searchTerm = null) }
        searchUsers()
    }

    private fun searchUsers() {
        with(uiState.value) {
            onLoading()
            viewModelScope.launch {
                searchUsersUseCase.invoke(
                    params = SearchUsersUseCase.Params(term = searchTerm),
                    onSuccess = ::onSearchFinished,
                    onError = ::onErrorOccurred
                )
            }
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
        updateState { it.copy(isLoading = false) }
    }
}

data class SearchUiState(
    val searchTerm: String? = null,
    val userResult: Iterable<UserInfo> = emptyList(),
    val isLoading: Boolean = false,
)