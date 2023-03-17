package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveCommentDTO
import com.dreamsoftware.artcollectibles.domain.models.CreateComment
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveCommentMapper: IOneSideMapper<CreateComment, SaveCommentDTO> {

    override fun mapInToOut(input: CreateComment): SaveCommentDTO = with(input) {
        SaveCommentDTO(
            uid, comment, userUid, tokenId
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateComment>): Iterable<SaveCommentDTO> =
        input.map(::mapInToOut)
}