package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.UserDTO
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.models.UserPreferences
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper
import java.math.BigInteger

class UserInfoMapper: IOneSideMapper<UserInfoMapper.InputData, UserInfo> {

    private companion object {
        const val DEFAULT_IS_PUBLIC_PROFILE_PREFERENCE = true
        const val DEFAULT_SHOW_SELLING_TOKENS_ROW_PREFERENCE = true
        const val DEFAULT_SHOW_LAST_TRANSACTIONS_OF_TOKENS_PREFERENCE = true
        const val DEFAULT_ALLOW_PUBLISH_COMMENTS_PREFERENCE = true
        const val DEFAULT_SHOW_ACCOUNT_BALANCE_PREFERENCE = true
    }

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
                instagramNick = instagramNick,
                tokensSoldCount = tokensSoldCount,
                tokensBoughtCount = tokensBoughtCount,
                tokensOwnedCount = tokensOwnedCount,
                tokensCreatedCount = tokensCreatedCount,
                preferences = UserPreferences(
                    isPublicProfile = isPublicProfile ?: DEFAULT_IS_PUBLIC_PROFILE_PREFERENCE,
                    showSellingTokensRow = showSellingTokensRow ?: DEFAULT_SHOW_SELLING_TOKENS_ROW_PREFERENCE,
                    showLastTransactionsOfTokens = showLastTransactionsOfTokens ?: DEFAULT_SHOW_LAST_TRANSACTIONS_OF_TOKENS_PREFERENCE,
                    allowPublishComments = allowPublishComments ?: DEFAULT_ALLOW_PUBLISH_COMMENTS_PREFERENCE,
                    showAccountBalance = showAccountBalance ?: DEFAULT_SHOW_ACCOUNT_BALANCE_PREFERENCE
                )
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