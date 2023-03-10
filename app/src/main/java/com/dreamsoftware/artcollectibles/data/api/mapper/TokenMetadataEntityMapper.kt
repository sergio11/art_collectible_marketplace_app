package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity
import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenMetadataEntityMapper(
    private val artCollectibleCategoryMapper: ArtCollectibleCategoryMapper
): IOneSideMapper<TokenMetadataEntityMapper.InputData, ArtCollectibleMetadata> {

    override fun mapInToOut(input: InputData): ArtCollectibleMetadata = with(input) {
        with(tokenMetadata) {
            ArtCollectibleMetadata(
                cid = cid,
                name = name,
                imageUrl = imageUrl,
                description = description.orEmpty(),
                tags = tags,
                createdAt = createdAt,
                authorAddress = authorAddress,
                category = artCollectibleCategoryMapper.mapInToOut(category)
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<ArtCollectibleMetadata> =
        input.map(::mapInToOut)

    data class InputData(
        val tokenMetadata: TokenMetadataEntity,
        val category: CategoryDTO
    )
}