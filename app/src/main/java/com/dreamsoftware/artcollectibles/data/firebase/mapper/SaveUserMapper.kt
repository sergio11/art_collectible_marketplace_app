package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveUserMapper : IOneSideMapper<SaveUserDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val NAME_KEY = "name"
        const val PROFESSIONAL_TITLE_KEY = "professionalTitle"
        const val WALLET_ADDRESS_KEY = "walletAddress"
        const val INFO_KEY = "info"
        const val CONTACT_KEY = "contact"
        const val PHOTO_KEY = "photo"
        const val BIRTHDATE_KEY = "birthdate"
        const val EXTERNAL_PROVIDER_AUTH_KEY = "externalProviderAuth"
        const val LOCATION_KEY = "location"
    }

    override fun mapInToOut(input: SaveUserDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            NAME_KEY to name,
            WALLET_ADDRESS_KEY to walletAddress,
            EXTERNAL_PROVIDER_AUTH_KEY to externalAuthProvider
        ).apply {
            professionalTitle?.let { put(PROFESSIONAL_TITLE_KEY, it) }
            contact?.let { put(CONTACT_KEY, it) }
            photoUrl?.let { put(PHOTO_KEY, it) }
            info?.let { put(INFO_KEY, it) }
            birthdate?.let { put(BIRTHDATE_KEY, it) }
            location?.let { put(LOCATION_KEY, it) }
        }
    }

    override fun mapInListToOutList(input: Iterable<SaveUserDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}