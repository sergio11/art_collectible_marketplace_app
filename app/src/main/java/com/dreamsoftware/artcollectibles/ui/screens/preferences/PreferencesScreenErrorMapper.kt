package com.dreamsoftware.artcollectibles.ui.screens.preferences

import android.content.Context
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.data.api.exception.GetCurrentBalanceException
import com.dreamsoftware.artcollectibles.data.api.exception.GetDetailException
import com.dreamsoftware.artcollectibles.data.api.exception.SaveUserException
import com.dreamsoftware.artcollectibles.utils.IErrorMapper

class PreferencesScreenErrorMapper(
    private val context: Context
): IErrorMapper {
    override fun mapToMessage(ex: Throwable): String = context.getString(when(ex) {
        is GetDetailException -> R.string.get_user_detail_error
        is GetCurrentBalanceException -> R.string.get_current_balance_error
        is SaveUserException -> R.string.save_user_error
        else -> R.string.generic_error_exception
    })
}