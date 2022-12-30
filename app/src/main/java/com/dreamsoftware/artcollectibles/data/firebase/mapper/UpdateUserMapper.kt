package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UpdateUserDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UpdateUserMapper: IOneSideMapper<UpdateUserDTO, Map<String, Any?>> {

    private companion object {
        const val NAME_KEY = "name"
        const val INFO_KEY = "info"
        const val CONTACT_KEY = "contact"
        const val PHOTO_KEY = "photo"
    }

    override fun mapInToOut(input: UpdateUserDTO): Map<String, Any?> = with(input) {
        hashMapOf<String, Any?>().apply {
            name?.let { NAME_KEY to it }
            info?.let { INFO_KEY to it }
            contact?.let { CONTACT_KEY to it }
            photoUrl?.let { PHOTO_KEY to it }
        }
    }

    override fun mapInListToOutList(input: Iterable<UpdateUserDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}