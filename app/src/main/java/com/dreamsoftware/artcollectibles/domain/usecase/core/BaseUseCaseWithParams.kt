package com.dreamsoftware.artcollectibles.domain.usecase.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCaseWithParams<ParamsType, ReturnType> {

    // We use the default dispatcher for useCases as Dispatchers.IO
    // but you can change it in your child class
    open val dispatcher: CoroutineDispatcher = Dispatchers.IO

    // This fun will be used to provide the actual implementation
    // in the child class
    abstract suspend fun onExecuted(params: ParamsType): ReturnType

    suspend operator fun invoke(params: ParamsType) = withContext(dispatcher) {
        onExecuted(params)
    }
}