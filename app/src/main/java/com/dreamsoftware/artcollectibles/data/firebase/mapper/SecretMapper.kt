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
        const val SECURITY_KEY_ALIAS = "secrets"
    }

    override fun mapInToOut(input: SecretDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            SECRET_KEY to cryptoUtils.encryptData(SECURITY_KEY_ALIAS, secret)
        )
    }

    override fun mapInListToOutList(input: Iterable<SecretDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): SecretDTO = with(input) {
        SecretDTO(
            uid = get(UID_KEY) as String,
            secret = cryptoUtils.decryptData(SECURITY_KEY_ALIAS, get(SECRET_KEY) as ByteArray)
        )
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<SecretDTO> =
        input.map(::mapOutToIn)
}