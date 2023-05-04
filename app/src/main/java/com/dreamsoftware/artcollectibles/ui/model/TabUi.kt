package com.dreamsoftware.artcollectibles.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TabUi<out T>(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    var isSelected: Boolean = false,
    val type: T
)
