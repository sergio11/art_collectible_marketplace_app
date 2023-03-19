package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CommentDTO
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CommentMapper: IOneSideMapper<CommentMapper.InputData, Comment> {

    override fun mapInToOut(input: InputData): Comment = with(input) {
        with(commentDTO) {
            Comment(
                uid = uid,
                text = comment,
                user = userInfoDTO,
                tokenId = tokenId,
                createdAt = createdAt
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<Comment> =
        input.map(::mapInToOut)

    data class InputData(
        val commentDTO: CommentDTO,
        val userInfoDTO: UserInfo
    )
}