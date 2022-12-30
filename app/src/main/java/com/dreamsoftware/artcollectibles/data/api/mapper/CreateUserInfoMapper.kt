package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.CreateUserDTO
import com.dreamsoftware.artcollectibles.domain.models.CreateUserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class CreateUserInfoMapper: IOneSideMapper<CreateUserInfo, CreateUserDTO> {

    override fun mapInToOut(input: CreateUserInfo): CreateUserDTO = with(input) {
        CreateUserDTO(
            uid = uid,
            name = name,
            contact = contact,
            walletAddress = walletAddress,
            photoUrl = photoUrl
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateUserInfo>): Iterable<CreateUserDTO> =
        input.map(::mapInToOut)

}