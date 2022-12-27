package com.dreamsoftware.artcollectibles.data.ipfs.models.request

import com.dreamsoftware.artcollectibles.data.ipfs.utils.TOKEN_AUTHOR_ADDRESS
import com.dreamsoftware.artcollectibles.data.ipfs.utils.TOKEN_DESCRIPTION_KEY
import com.dreamsoftware.artcollectibles.data.ipfs.utils.TOKEN_OWNER_ADDRESS
import com.squareup.moshi.Json

/**
 * { "name": "MyFile",  "keyvalues": { "company": "Pinata" } }
 */
data class FileMetadataDTO(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "keyvalues")
    val keyValues: MutableMap<String, String> = hashMapOf()
) {

    var description: String
        get() = keyValues[TOKEN_DESCRIPTION_KEY].orEmpty()
        set(value) {
            keyValues[TOKEN_DESCRIPTION_KEY] = value
        }

    var ownerAddress: String
        get() = keyValues[TOKEN_OWNER_ADDRESS].orEmpty()
        set(value) {
            keyValues[TOKEN_OWNER_ADDRESS] = value
        }

    var authorAddress: String
        get() = keyValues[TOKEN_AUTHOR_ADDRESS].orEmpty()
        set(value) {
            keyValues[TOKEN_AUTHOR_ADDRESS] = value
        }
}
