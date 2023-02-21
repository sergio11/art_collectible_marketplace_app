package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenMetadataEntityMapper: IOneSideMapper<TokenMetadataEntity, ArtCollectibleMetadata> {

    override fun mapInToOut(input: TokenMetadataEntity): ArtCollectibleMetadata = with(input) {
        ArtCollectibleMetadata(
            cid = cid,
            name = name,
            imageUrl = imageUrl,
            description = description.orEmpty(),
            tags = tags,
            createdAt = createdAt,
            authorAddress = authorAddress
        )
    }

    override fun mapInListToOutList(input: Iterable<TokenMetadataEntity>): Iterable<ArtCollectibleMetadata> =
        input.map(::mapInToOut)
}