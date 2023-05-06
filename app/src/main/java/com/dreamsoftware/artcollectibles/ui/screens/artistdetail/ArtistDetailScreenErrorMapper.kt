package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import android.content.Context
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.utils.IErrorMapper

class ArtistDetailScreenErrorMapper(
    private val context: Context
): IErrorMapper {
    override fun mapToMessage(ex: Throwable): String = context.getString(when(ex) {
        is GetDetailException -> R.string.get_user_detail_error
        is FollowUserException -> R.string.follow_user_error
        is UnFollowUserException -> R.string.un_follow_user_error
        is CheckFollowersUserException -> R.string.my_tokens_tab_tokens_owned_error
        is GetTokensOwnedDataException -> R.string.get_tokens_owned_error
        is GetTokensCreatedDataException -> R.string.get_tokens_created_error
        is GetCurrentBalanceException -> R.string.get_current_balance_error
        else -> R.string.generic_error_exception
    })
}