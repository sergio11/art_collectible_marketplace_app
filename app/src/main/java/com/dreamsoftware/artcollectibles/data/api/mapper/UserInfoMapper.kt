package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IMapper

class UserInfoMapper: IMapper<UserDTO, UserInfo> {

    override fun mapInToOut(input: UserDTO): UserInfo = with(input) {
        UserInfo(
            uid = uid,
            name = name,
            info = info.orEmpty(),
            contact = contact.orEmpty(),
            walletAddress = walletAddress
        )
    }

    override fun mapInListToOutList(input: Iterable<UserDTO>): Iterable<UserInfo> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: UserInfo): UserDTO = with(input) {
        UserDTO(
            uid = uid,
            name = name,
            info = info,
            contact = contact,
            walletAddress = walletAddress
        )
    }

    override fun mapOutListToInList(input: Iterable<UserInfo>): Iterable<UserDTO> =
        input.map(::mapOutToIn)
}