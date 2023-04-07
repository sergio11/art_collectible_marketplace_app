package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CategoryDTO
import com.dreamsoftware.artcollectibles.utils.ISimpleOneSideMapper

class CategoriesMapper: ISimpleOneSideMapper<Map<String, Any?>, Iterable<CategoryDTO>> {

    private companion object {
        const val NAME_KEY = "name"
        const val IMAGE_KEY = "image"
        const val DATA_KEY = "data"
        const val DESCRIPTION_KEY = "description"
    }

    override fun mapInToOut(input: Map<String, Any?>): Iterable<CategoryDTO> = with(input) {
        (get(DATA_KEY) as? Map<String, Map<String, Any>>)?.entries?.map {
            CategoryDTO(
                uid = it.key,
                name = it.value[NAME_KEY] as String,
                imageUrl = it.value[IMAGE_KEY] as String,
                description = it.value[DESCRIPTION_KEY] as String
            )
        }.orEmpty()
    }
}