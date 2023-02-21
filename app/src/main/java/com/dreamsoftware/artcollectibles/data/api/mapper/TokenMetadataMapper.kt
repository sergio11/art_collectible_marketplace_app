package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenMetadataMapper: IOneSideMapper<TokenMetadataDTO, ArtCollectibleMetadata> {

    override fun mapInToOut(input: TokenMetadataDTO): ArtCollectibleMetadata = with(input) {
        ArtCollectibleMetadata(
            cid = cid,
            name = name,
            description = description.orEmpty(),
            createdAt = createdAt,
            imageUrl = imageUrl,
            tags = tags,
            authorAddress = authorAddress
        )
    }

    override fun mapInListToOutList(input: Iterable<TokenMetadataDTO>): Iterable<ArtCollectibleMetadata> =
        input.map(::mapInToOut)
}