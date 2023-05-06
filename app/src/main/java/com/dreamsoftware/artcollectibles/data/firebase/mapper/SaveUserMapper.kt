package com.dreamsoftware.artcollectibles.data.firebase.mapper

import com.dreamsoftware.artcollectibles.data.firebase.model.SaveUserDTO
import com.dreamsoftware.artcollectibles.utils.IOneSideMapper

class SaveUserMapper : IOneSideMapper<SaveUserDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val NAME_KEY = "name"
        const val PROFESSIONAL_TITLE_KEY = "professionalTitle"
        const val WALLET_ADDRESS_KEY = "walletAddress"
        const val INFO_KEY = "info"
        const val CONTACT_KEY = "contact"
        const val PHOTO_KEY = "photo"
        const val BIRTHDATE_KEY = "birthdate"
        const val EXTERNAL_PROVIDER_AUTH_KEY = "externalProviderAuth"
        const val LOCATION_KEY = "location"
        const val COUNTRY_KEY = "country"
        const val TAGS_KEY = "tags"
        const val INSTAGRAM_NICK_KEY = "instagramNick"
        const val IS_PUBLIC_PROFILE_KEY = "isPublicProfile"
        const val SHOW_SELLING_TOKENS_ROW_KEY = "showSellingTokensRow"
        const val SHOW_LAST_TRANSACTIONS_OF_TOKENS_KEY = "showLastTransactionsOfTokens"
        const val ALLOW_PUBLISH_COMMENTS_KEY = "allowPublishComments"
        const val SHOW_ACCOUNT_BALANCE_KEY = "showAccountBalance"
    }

    override fun mapInToOut(input: SaveUserDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            NAME_KEY to name,
            WALLET_ADDRESS_KEY to walletAddress,
            EXTERNAL_PROVIDER_AUTH_KEY to externalAuthProvider
        ).apply {
            professionalTitle?.let { put(PROFESSIONAL_TITLE_KEY, it) }
            contact?.let { put(CONTACT_KEY, it) }
            photoUrl?.let { put(PHOTO_KEY, it) }
            info?.let { put(INFO_KEY, it) }
            birthdate?.let { put(BIRTHDATE_KEY, it) }
            location?.let { put(LOCATION_KEY, it) }
            country?.let { put(COUNTRY_KEY, it) }
            tags?.let { put(TAGS_KEY, it.joinToString(",")) }
            instagramNick?.let { put(INSTAGRAM_NICK_KEY, it) }
            isPublicProfile?.let { put(IS_PUBLIC_PROFILE_KEY, it.toString()) }
            showSellingTokensRow?.let { put(SHOW_SELLING_TOKENS_ROW_KEY, it.toString()) }
            showLastTransactionsOfTokens?.let { put(SHOW_LAST_TRANSACTIONS_OF_TOKENS_KEY, it.toString()) }
            allowPublishComments?.let { put(ALLOW_PUBLISH_COMMENTS_KEY, it.toString()) }
            showAccountBalance?.let { put(SHOW_ACCOUNT_BALANCE_KEY, it.toString()) }
        }
    }

    override fun mapInListToOutList(input: Iterable<SaveUserDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}