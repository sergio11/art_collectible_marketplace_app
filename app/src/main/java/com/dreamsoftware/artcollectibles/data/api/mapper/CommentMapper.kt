package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CommentDTO
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CommentMapper: IOneSideMapper<CommentMapper.InputData, Comment> {

    override fun mapInToOut(input: InputData): Comment = with(input) {
        Comment(
            uid = commentDTO.uid,
            text = commentDTO.comment,
            user = userInfoDTO,
            tokenId = commentDTO.tokenId
        )
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<Comment> =
        input.map(::mapInToOut)

    data class InputData(
        val commentDTO: CommentDTO,
        val userInfoDTO: UserInfo
    )
}