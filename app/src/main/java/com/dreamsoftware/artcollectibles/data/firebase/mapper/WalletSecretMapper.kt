package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.WalletSecretDTO
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IMapper

class WalletSecretMapper(
    private val cryptoUtils: CryptoUtils
) : IMapper<WalletSecretDTO, Map<String, Any?>> {

    private companion object {
        const val SECRET_KEY = "secret"
        const val NAME_KEY = "name"
        const val UID_KEY = "userUid"
    }

    override fun mapInToOut(input: WalletSecretDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to userUid,
            SECRET_KEY to cryptoUtils.encryptAndEncode(secret),
            NAME_KEY to cryptoUtils.encryptAndEncode(name)
        )
    }

    override fun mapInListToOutList(input: Iterable<WalletSecretDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): WalletSecretDTO = with(input) {
        WalletSecretDTO(
            userUid = get(UID_KEY) as String,
            secret = cryptoUtils.decodeAndDecrypt(get(SECRET_KEY) as String),
            name = cryptoUtils.decodeAndDecrypt(get(NAME_KEY) as String)
        )
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<WalletSecretDTO> =
        input.map(::mapOutToIn)
}