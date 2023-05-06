package com.dreamsoftware.artcollectibles.ui.screens.discovery

import android.content.Context
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.data.api.exception.GetCategoriesDataException
import com.dreamsoftware.artcollectibles.data.api.exception.SearchUserException
import com.dreamsoftware.artcollectibles.utils.IErrorMapper

class DiscoveryScreenErrorMapper(
    private val context: Context
): IErrorMapper {
    override fun mapToMessage(ex: Exception): String = context.getString(when(ex) {
        is GetCategoriesDataException -> R.string.discovery_get_art_categories_error
        is SearchUserException -> R.string.discovery_search_users_error
        else -> R.string.generic_error_exception
    })
}