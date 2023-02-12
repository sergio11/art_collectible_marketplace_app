package com.dreamsoftware.artcollectibles.domain.usecase.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Simple use case exposing result as a flow.
 * Result flow will emit null while the action has not been triggered
 */
abstract class BaseFlowUseCase<T> {

    /**
     * Trigger for the action which can be done in this request
     */
    private val _trigger = MutableStateFlow(true)

    /**
     * Exposes result of this use case
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val resultFlow: Flow<T> = _trigger.flatMapLatest {
        performAction()
    }

    protected abstract suspend fun performAction() : Flow<T>

    /**
     * Triggers the execution of this use case
     */
    suspend fun launch() {
        _trigger.emit(!(_trigger.value))
    }
}