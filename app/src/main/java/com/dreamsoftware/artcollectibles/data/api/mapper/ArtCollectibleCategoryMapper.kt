package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class ArtCollectibleCategoryMapper: IOneSideMapper<CategoryDTO, ArtCollectibleCategory> {

    override fun mapInToOut(input: CategoryDTO): ArtCollectibleCategory = with(input) {
        ArtCollectibleCategory(
            uid, name, imageUrl
        )
    }

    override fun mapInListToOutList(input: Iterable<CategoryDTO>): Iterable<ArtCollectibleCategory> =
        input.map(::mapInToOut)
}