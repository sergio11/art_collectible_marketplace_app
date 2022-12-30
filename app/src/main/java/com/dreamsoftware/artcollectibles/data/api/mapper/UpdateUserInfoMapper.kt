package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UpdateUserDTO
import com.dreamsoftware.artcollectibles.domain.models.UpdateUserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UpdateUserInfoMapper : IOneSideMapper<UpdateUserInfo, UpdateUserDTO> {

    override fun mapInToOut(input: UpdateUserInfo): UpdateUserDTO = with(input) {
        UpdateUserDTO(
            uid = uid,
            name = name,
            info = info,
            contact = contact,
            photoUrl = photoUrl
        )
    }

    override fun mapInListToOutList(input: Iterable<UpdateUserInfo>): Iterable<UpdateUserDTO> =
        input.map(::mapInToOut)
}