package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.WalletMetadataDTO
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import com.dreamsoftware.artcollectibles.utils.IMapper

class WalletMetadataMapper(
    private val cryptoUtils: CryptoUtils,
    private val applicationAware: IApplicationAware
) : IMapper<WalletMetadataDTO, Map<String, Any?>> {

    private companion object {
        const val SECRET_KEY = "secret"
        const val NAME_KEY = "name"
        const val UID_KEY = "userUid"
        const val PATH_KEY = "path"
    }

    override fun mapInToOut(input: WalletMetadataDTO): Map<String, Any?> = with(input) {
        val key = applicationAware.getUserSecretKey()
        hashMapOf(
            UID_KEY to userUid,
            SECRET_KEY to cryptoUtils.encryptAndEncode(key, secret),
            NAME_KEY to cryptoUtils.encryptAndEncode(key, name),
            PATH_KEY to walletUri
        )
    }

    override fun mapInListToOutList(input: Iterable<WalletMetadataDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): WalletMetadataDTO = with(input) {
        val key = applicationAware.getUserSecretKey()
        WalletMetadataDTO(
            userUid = get(UID_KEY) as String,
            secret = cryptoUtils.decodeAndDecrypt(key, get(SECRET_KEY) as String),
            name = cryptoUtils.decodeAndDecrypt(key, get(NAME_KEY) as String),
            walletUri = get(NAME_KEY) as String
        )
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<WalletMetadataDTO> =
        input.map(::mapOutToIn)
}