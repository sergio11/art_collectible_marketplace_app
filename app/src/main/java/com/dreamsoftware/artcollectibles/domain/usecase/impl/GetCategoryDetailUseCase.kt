package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleCategoryRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetCategoryDetailUseCase(
    private val artCollectibleCategoryRepository: IArtCollectibleCategoryRepository
): BaseUseCaseWithParams<GetCategoryDetailUseCase.Params, ArtCollectibleCategory>() {

    override suspend fun onExecuted(params: Params): ArtCollectibleCategory =
        artCollectibleCategoryRepository.getByUid(params.uid)

    data class Params(
        val uid: String
    )
}