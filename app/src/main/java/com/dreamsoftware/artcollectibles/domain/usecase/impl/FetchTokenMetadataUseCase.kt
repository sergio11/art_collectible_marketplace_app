package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class FetchTokenMetadataUseCase(
    private val tokenMetadataRepository: ITokenMetadataRepository
): BaseUseCaseWithParams<FetchTokenMetadataUseCase.Params, ArtCollectibleMetadata>() {

    override suspend fun onExecuted(params: Params): ArtCollectibleMetadata = with(params) {
        tokenMetadataRepository.fetchByCid(metadataCid)
    }

    data class Params(
        val metadataCid: String
    )
}