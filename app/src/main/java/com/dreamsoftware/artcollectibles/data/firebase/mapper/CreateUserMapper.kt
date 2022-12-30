package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CreateUserDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CreateUserMapper: IOneSideMapper<CreateUserDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val NAME_KEY = "name"
        const val WALLET_ADDRESS_KEY = "walletAddress"
        const val CONTACT_KEY = "contact"
        const val PHOTO_KEY = "photo"
    }

    override fun mapInToOut(input: CreateUserDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            NAME_KEY to name,
            WALLET_ADDRESS_KEY to walletAddress,
        ).apply {
            contact?.let { CONTACT_KEY to it }
            photoUrl?.let { PHOTO_KEY to it }
        }
    }

    override fun mapInListToOutList(input: Iterable<CreateUserDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}