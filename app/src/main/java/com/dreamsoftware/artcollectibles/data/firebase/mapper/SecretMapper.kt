package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IMapper

class SecretMapper(
    private val cryptoUtils: CryptoUtils
) : IMapper<SecretDTO, Map<String, Any?>> {

    private companion object {
        const val SECRET_KEY = "secret"
        const val UID_KEY = "uid"
    }

    override fun mapInToOut(input: SecretDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            SECRET_KEY to cryptoUtils.encryptAndEncode(secret)
        )
    }

    override fun mapInListToOutList(input: Iterable<SecretDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): SecretDTO = with(input) {
        SecretDTO(
            uid = get(UID_KEY) as String,
            secret = cryptoUtils.decodeAndDecrypt(get(SECRET_KEY) as String)
        )
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<SecretDTO> =
        input.map(::mapOutToIn)
}