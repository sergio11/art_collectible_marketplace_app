package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class CreateArtCollectibleUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCaseWithParams<CreateArtCollectibleUseCase.Params, ArtCollectible>() {

    override suspend fun onExecuted(params: Params): ArtCollectible = with(params) {
        artCollectibleRepository.create(CreateArtCollectible(
            royalty = royalty,
            metadata = CreateArtCollectibleMetadata(
                name = name,
                description = description,
                fileUri = imagePath,
                mediaType = "image/$mediaType",
                tags = tags
            )
        ))
    }

    data class Params(
        val imagePath: String,
        val mediaType: String,
        val name: String,
        val description: String? = null,
        val royalty: Long,
        val tags: List<String>
    )
}