package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.IEvent
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseFlowUseCase
import kotlinx.coroutines.flow.Flow

class GetEventsUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseFlowUseCase<IEvent>() {

    override suspend fun performAction(): Flow<IEvent> =
        artCollectibleRepository.observeArtCollectibleMintedEvents()
}