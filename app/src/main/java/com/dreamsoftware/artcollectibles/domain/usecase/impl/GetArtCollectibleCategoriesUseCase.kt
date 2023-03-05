package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleCategoryRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetArtCollectibleCategoriesUseCase(
    private val artCollectibleCategoryRepository: IArtCollectibleCategoryRepository
): BaseUseCase<Iterable<ArtCollectibleCategory>>() {

    override suspend fun onExecuted(): Iterable<ArtCollectibleCategory> =
        artCollectibleCategoryRepository.getAll()
}