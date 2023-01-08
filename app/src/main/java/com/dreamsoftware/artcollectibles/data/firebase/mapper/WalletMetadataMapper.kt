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
        const val PASSWORD_KEY = "password"
        const val NAME_KEY = "name"
        const val UID_KEY = "userUid"
        const val PATH_KEY = "path"
    }

    override fun mapInToOut(input: WalletMetadataDTO): Map<String, Any?> = with(input) {
        with(applicationAware.getUserSecret()) {
            hashMapOf(
                UID_KEY to userUid,
                PASSWORD_KEY to cryptoUtils.encryptAndEncode(secret, salt, password),
                NAME_KEY to cryptoUtils.encryptAndEncode(secret, salt, name),
                PATH_KEY to walletUri
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<WalletMetadataDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: Map<String, Any?>): WalletMetadataDTO = with(input) {
        with(applicationAware.getUserSecret()) {
            WalletMetadataDTO(
                userUid = get(UID_KEY) as String,
                password = cryptoUtils.decodeAndDecrypt(secret, salt, get(PASSWORD_KEY) as String),
                name = cryptoUtils.decodeAndDecrypt(secret, salt, get(NAME_KEY) as String),
                walletUri = get(PATH_KEY) as String
            )
        }
    }

    override fun mapOutListToInList(input: Iterable<Map<String, Any?>>): Iterable<WalletMetadataDTO> =
        input.map(::mapOutToIn)
}