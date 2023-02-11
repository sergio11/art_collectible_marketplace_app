package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveUserInfoMapper: IOneSideMapper<UserInfo, SaveUserDTO> {

    override fun mapInToOut(input: UserInfo): SaveUserDTO = with(input) {
        SaveUserDTO(
            uid = uid,
            name = name,
            professionalTitle = professionalTitle,
            walletAddress = walletAddress,
            info = info,
            contact = contact,
            photoUrl = photoUrl,
            birthdate = birthdate,
            externalAuthProvider = externalProviderAuthType?.name
        )
    }

    override fun mapInListToOutList(input: Iterable<UserInfo>): Iterable<SaveUserDTO> =
        input.map(::mapInToOut)

}