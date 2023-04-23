package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveNotificationDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.util.*

class SaveNotificationMapper: IOneSideMapper<SaveNotificationDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val TITLE_KEY = "title"
        const val DESCRIPTION_KEY = "description"
        const val USER_UID_KEY = "userUid"
        const val TOKEN_ID_KEY = "tokenId"
        const val CREATED_AT_KEY = "createdAt"
    }

    override fun mapInToOut(input: SaveNotificationDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            TITLE_KEY to title,
            DESCRIPTION_KEY to description,
            USER_UID_KEY to userUid,
            TOKEN_ID_KEY to tokenId.toString(),
            CREATED_AT_KEY to Date().time.toString()
        )
    }

    override fun mapInListToOutList(input: Iterable<SaveNotificationDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}