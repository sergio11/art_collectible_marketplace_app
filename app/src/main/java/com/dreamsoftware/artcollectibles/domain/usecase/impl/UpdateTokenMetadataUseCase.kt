package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.ITokenMetadataRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.models.UpdateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class UpdateTokenMetadataUseCase(
    private val tokenMetadataRepository: ITokenMetadataRepository
): BaseUseCaseWithParams<UpdateTokenMetadataUseCase.Params, ArtCollectibleMetadata>() {

    override suspend fun onExecuted(params: Params): ArtCollectibleMetadata = with(params) {
        tokenMetadataRepository.update(UpdateArtCollectibleMetadata(
            cid = cid,
            name = name,
            description = description,
            tags = tags
        ))
    }

    data class Params(
        val cid: String,
        val name: String,
        val description: String? = null,
        val tags: List<String>
    )
}