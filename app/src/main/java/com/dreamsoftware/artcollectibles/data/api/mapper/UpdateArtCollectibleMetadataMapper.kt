package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.models.UpdateTokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.UpdateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UpdateArtCollectibleMetadataMapper: IOneSideMapper<UpdateArtCollectibleMetadata, UpdateTokenMetadataDTO> {

    override fun mapInToOut(input: UpdateArtCollectibleMetadata): UpdateTokenMetadataDTO = with(input) {
        UpdateTokenMetadataDTO(
            cid = cid,
            name = name,
            description = description,
            tags = tags
        )
    }

    override fun mapInListToOutList(input: Iterable<UpdateArtCollectibleMetadata>): Iterable<UpdateTokenMetadataDTO> =
        input.map(::mapInToOut)
}