package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(): SupportViewModel<ArtistDetailUiState>() {

    override fun onGetDefaultState(): ArtistDetailUiState = ArtistDetailUiState()

    fun loadDetail(uid: String) {}
}

data class ArtistDetailUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null
)