package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.NotificationDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.math.BigInteger
import java.util.*

class NotificationMapper: IOneSideMapper<Map<String, Any?>, NotificationDTO> {

    private companion object {
        const val UID_KEY = "uid"
        const val TITLE_KEY = "title"
        const val DESCRIPTION_KEY = "description"
        const val USER_UID_KEY = "userUid"
        const val TOKEN_ID_KEY = "tokenId"
        const val CREATED_AT_KEY = "createdAt"
    }

    override fun mapInToOut(input: Map<String, Any?>): NotificationDTO = with(input) {
        NotificationDTO(
            uid = get(UID_KEY) as String,
            title = get(TITLE_KEY) as String,
            description = get(DESCRIPTION_KEY) as String,
            userUid = get(USER_UID_KEY) as String,
            tokenId = BigInteger(get(TOKEN_ID_KEY) as String),
            createdAt = Date((get(CREATED_AT_KEY) as String).toLong())
        )
    }

    override fun mapInListToOutList(input: Iterable<Map<String, Any?>>): Iterable<NotificationDTO> =
        input.map(::mapInToOut)
}