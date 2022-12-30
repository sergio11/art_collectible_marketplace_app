package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.utils.IMapper

class UserMapper: IMapper<UserDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val NAME_KEY = "name"
        const val INFO_KEY = "info"
        const val WALLET_ADDRESS_KEY = "walletAddress"
        const val CONTACT_KEY = "contact"
    }

    override fun mapInToOut(input: UserDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            NAME_KEY to name,
            INFO_KEY to info,
            WALLET_ADDRESS_KEY to walletAddress,
            CONTACT_KEY to contact
        )
    }

    override fun mapInListToOutList(input: Iterable<UserDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): UserDTO = with(input) {
        UserDTO(
            uid = get(UID_KEY) as String,
            name = get(NAME_KEY) as String,
            info = get(INFO_KEY) as? String,
            walletAddress = get(WALLET_ADDRESS_KEY) as String,
            contact = get(CONTACT_KEY) as? String
        )
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<UserDTO> =
        input.map(::mapOutToIn)
}