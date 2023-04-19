package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.math.BigInteger

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
                tags = tags,
                location = location,
                country = country,
                followers = followers,
                following = following,
                tokensSoldCount = tokensSoldCount,
                tokensBoughtCount = tokensBoughtCount,
                tokensOwnedCount = tokensOwnedCount,
                tokensCreatedCount = tokensCreatedCount
            )
        }
    }

    override fun mapInListToOutList(input: Iterable<InputData>): Iterable<UserInfo> =
        input.map(::mapInToOut)

    data class InputData(
        val user: UserDTO,
        val followers: Long = 0L,
        val following: Long = 0L,
        val tokensSoldCount: BigInteger = BigInteger.ZERO,
        val tokensBoughtCount: BigInteger = BigInteger.ZERO,
        val tokensOwnedCount: BigInteger = BigInteger.ZERO,
        val tokensCreatedCount: BigInteger = BigInteger.ZERO
    )
}