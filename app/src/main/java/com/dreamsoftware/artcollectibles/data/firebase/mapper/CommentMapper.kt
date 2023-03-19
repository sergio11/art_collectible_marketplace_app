package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CommentDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.math.BigInteger
import java.util.Date

class CommentMapper: IOneSideMapper<Map<String, Any?>, CommentDTO> {

    private companion object {
        const val UID_KEY = "uid"
        const val COMMENT_KEY = "comment"
        const val USER_UID_KEY = "userUid"
        const val TOKEN_ID_KEY = "tokenId"
        const val CREATED_AT_KEY = "createdAt"
    }

    override fun mapInToOut(input: Map<String, Any?>): CommentDTO = with(input) {
        CommentDTO(
            uid = get(UID_KEY) as String,
            comment = get(COMMENT_KEY) as String,
            userUid = get(USER_UID_KEY) as String,
            tokenId = BigInteger(get(TOKEN_ID_KEY) as String),
            createdAt = Date((get(CREATED_AT_KEY) as String).toLong())
        )
    }

    override fun mapInListToOutList(input: Iterable<Map<String, Any?>>): Iterable<CommentDTO> =
        input.map(::mapInToOut)
}