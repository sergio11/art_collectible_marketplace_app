package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetArtCollectiblesByCategoryUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCaseWithParams<GetArtCollectiblesByCategoryUseCase.Params, Iterable<ArtCollectible>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectible> =
        artCollectibleRepository.getTokensByCategory(categoryUid = params.categoryUid)

    data class Params(
        val categoryUid: String
    )
}