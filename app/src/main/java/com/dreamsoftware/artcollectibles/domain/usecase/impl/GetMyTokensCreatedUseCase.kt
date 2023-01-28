package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetMyTokensCreatedUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCase<Iterable<ArtCollectible>>() {
    override suspend fun onExecuted(): Iterable<ArtCollectible> =
        artCollectibleRepository.getTokensCreated()
}