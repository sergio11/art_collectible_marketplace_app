package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.models.CreateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CreateArtCollectibleMetadataMapper: IOneSideMapper<CreateArtCollectibleMetadata, CreateTokenMetadataDTO> {

    override fun mapInToOut(input: CreateArtCollectibleMetadata): CreateTokenMetadataDTO = with(input) {
        CreateTokenMetadataDTO(
            name = name,
            description = description,
            fileUri = fileUri,
            mediaType = mediaType,
            tags = tags
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateArtCollectibleMetadata>): Iterable<CreateTokenMetadataDTO> =
        input.map(::mapInToOut)
}