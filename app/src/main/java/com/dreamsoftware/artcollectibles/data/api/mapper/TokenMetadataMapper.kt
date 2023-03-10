package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO
import com.dreamsoftware.artcollectibles.data.ipfs.models.TokenMetadataDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class TokenMetadataMapper(
    private val artCollectibleCategoryMapper: ArtCollectibleCategoryMapper
): IOneSideMapper<TokenMetadataMapper.InputData, ArtCollectibleMetadata> {

    override fun mapInToOut(input: InputData): ArtCollectibleMetadata = with(input) {
        with(tokenMetadata) {
            ArtCollectibleMetadata(
                cid = cid,
                name = name,
                description = description.orEmpty(),
                createdAt = createdAt,
                imageUrl = imageUrl,
                tags = tags,
                authorAddress = authorAddress,
                category = artCollectibleCategoryMapper.mapInToOut(category)
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<ArtCollectibleMetadata> =
        input.map(::mapInToOut)

    data class InputData(
        val tokenMetadata: TokenMetadataDTO,
        val category: CategoryDTO
    )
}