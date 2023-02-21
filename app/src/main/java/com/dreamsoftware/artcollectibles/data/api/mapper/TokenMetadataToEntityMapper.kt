package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenMetadataToEntityMapper: IOneSideMapper<TokenMetadataDTO, TokenMetadataEntity> {

    override fun mapInToOut(input: TokenMetadataDTO): TokenMetadataEntity = with(input) {
        TokenMetadataEntity(
            cid = cid,
            name = name,
            description = description,
            createdAt = createdAt,
            imageUrl = imageUrl,
            tags = tags,
            authorAddress = authorAddress
        )
    }

    override fun mapInListToOutList(input: Iterable<TokenMetadataDTO>): Iterable<TokenMetadataEntity> =
        input.map(::mapInToOut)

}