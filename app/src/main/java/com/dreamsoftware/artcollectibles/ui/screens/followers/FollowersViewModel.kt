package com.dreamsoftware.artcollectibles.ui.screens.followers

import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Followers View Model
 */
@HiltViewModel
class FollowersViewModel @Inject constructor() : SupportViewModel<FollowersUiState>() {

    override fun onGetDefaultState(): FollowersUiState = FollowersUiState()

    fun load() {

    }
}

data class FollowersUiState(
    val isLoading: Boolean = false,
    val userResult: Iterable<UserInfo> = emptyList()
)