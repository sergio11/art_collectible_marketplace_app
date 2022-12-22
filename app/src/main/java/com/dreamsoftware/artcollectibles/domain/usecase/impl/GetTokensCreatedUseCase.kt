package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetTokensCreatedUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCase<Iterable<ArtCollectible>>() {
    override suspend fun onExecuted(): Iterable<ArtCollectible> =
        artCollectibleRepository.getTokensCreated()
}