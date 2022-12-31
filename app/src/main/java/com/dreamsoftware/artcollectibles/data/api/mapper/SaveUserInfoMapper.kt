package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.domain.models.SaveUserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveUserInfoMapper: IOneSideMapper<SaveUserInfo, SaveUserDTO> {

    override fun mapInToOut(input: SaveUserInfo): SaveUserDTO = with(input) {
        SaveUserDTO(
            uid = uid,
            name = name,
            walletAddress = walletAddress,
            info = info,
            contact = contact,
            photoUrl = photoUrl
        )
    }

    override fun mapInListToOutList(input: Iterable<SaveUserInfo>): Iterable<SaveUserDTO> =
        input.map(::mapInToOut)

}