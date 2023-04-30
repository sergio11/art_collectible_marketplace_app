package com.dreamsoftware.artcollectibles.ui.screens.preferences

import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(): SupportViewModel<PreferencesUiState>() {

    override fun onGetDefaultState(): PreferencesUiState = PreferencesUiState()
}

data class PreferencesUiState(
    val isLoading: Boolean = false,
)