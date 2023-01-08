package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import com.dreamsoftware.artcollectibles.utils.IMapper

class SecretMapper(
    private val cryptoUtils: CryptoUtils,
    private val applicationAware: IApplicationAware
) : IMapper<SecretDTO, Map<String, Any?>> {

    private companion object {
        const val SECRET_KEY = "secret"
        const val SALT_KEY = "salt"
        const val UID_KEY = "userUid"
    }

    override fun mapInToOut(input: SecretDTO): Map<String, Any?> = with(input) {
        val masterPBE = applicationAware.getMasterSecret()
        hashMapOf(
            UID_KEY to userUid,
            SECRET_KEY to cryptoUtils.encryptAndEncode(masterPBE.secret, masterPBE.salt, secret),
            SALT_KEY to cryptoUtils.encryptAndEncode(masterPBE.secret, masterPBE.salt, salt)
        )
    }

    override fun mapInListToOutList(input: Iterable<SecretDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): SecretDTO = with(input) {
        val masterPBE = applicationAware.getMasterSecret()
        SecretDTO(
            userUid = get(UID_KEY) as String,
            secret = cryptoUtils.decodeAndDecrypt(masterPBE.secret, masterPBE.salt, get(SECRET_KEY) as String),
            salt = cryptoUtils.decodeAndDecrypt(masterPBE.secret, masterPBE.salt, get(SALT_KEY) as String)
        )
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<SecretDTO> =
        input.map(::mapOutToIn)
}