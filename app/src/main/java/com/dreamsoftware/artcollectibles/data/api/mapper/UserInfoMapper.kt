package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class UserInfoMapper: IOneSideMapper<UserInfoMapper.InputData, UserInfo> {

    override fun mapInToOut(input: InputData): UserInfo = with(input) {
        with(user) {
            UserInfo(
                uid = uid,
                name = name,
                professionalTitle = professionalTitle,
                info = info,
                contact = contact.orEmpty(),
                walletAddress = walletAddress,
                photoUrl = photoUrl,
                birthdate = birthdate,
                externalProviderAuthType = externalProviderAuth?.let {
                    enumValueOf<ExternalProviderAuthTypeEnum>(it)
                },
                location = location,
                followers = followers,
                following = following
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<UserInfo> =
        input.map(::mapInToOut)

    data class InputData(
        val user: UserDTO,
        val followers: Long,
        val following: Long
    )
}