package com.dreamsoftware.artcollectibles.ui.screens.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class SupportViewModel<STATE>: ViewModel() {

    private val _uiState: MutableStateFlow<STATE> by lazy {
        MutableStateFlow(onGetDefaultState())
    }
    val uiState: StateFlow<STATE> = _uiState

    abstract fun onGetDefaultState(): STATE

    protected fun updateState(reducer: (currentState: STATE) -> STATE) {
        _uiState.value = reducer(_uiState.value)
    }
}