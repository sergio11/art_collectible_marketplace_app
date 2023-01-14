package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveUserMapper : IOneSideMapper<SaveUserDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val NAME_KEY = "name"
        const val WALLET_ADDRESS_KEY = "walletAddress"
        const val INFO_KEY = "info"
        const val CONTACT_KEY = "contact"
        const val PHOTO_KEY = "photo"
        const val EXTERNAL_PROVIDER_AUTH = "external_provider_auth"
    }

    override fun mapInToOut(input: SaveUserDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            NAME_KEY to name,
            WALLET_ADDRESS_KEY to walletAddress,
            EXTERNAL_PROVIDER_AUTH to externalAuthProvider
        ).apply {
            contact?.let { put(CONTACT_KEY, it) }
            photoUrl?.let { put(PHOTO_KEY, it) }
            info?.let { put(INFO_KEY, it) }
        }
    }

    override fun mapInListToOutList(input: Iterable<SaveUserDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}