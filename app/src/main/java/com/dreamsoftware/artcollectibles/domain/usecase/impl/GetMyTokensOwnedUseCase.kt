package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetMyTokensOwnedUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCase<Iterable<ArtCollectible>>() {
    override suspend fun onExecuted(): Iterable<ArtCollectible> =
        artCollectibleRepository.getTokensOwned()
}