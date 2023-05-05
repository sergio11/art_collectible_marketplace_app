package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel

@Composable
fun <T, VM: SupportViewModel<T>>produceUiState(initialState: T, viewModel: VM, lifecycle: Lifecycle): State<T> =
    produceState(
        initialValue = initialState,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }