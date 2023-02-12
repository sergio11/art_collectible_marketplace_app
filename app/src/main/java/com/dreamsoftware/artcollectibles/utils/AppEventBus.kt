package com.dreamsoftware.artcollectibles.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppEventBus {

    enum class AppEvent {
        SIGN_OUT,
        SIGN_IN
    }

    private val _events = MutableSharedFlow<AppEvent>()
    val events = _events.asSharedFlow()

    suspend fun invokeEvent(event: AppEvent) = _events.emit(event)
}


