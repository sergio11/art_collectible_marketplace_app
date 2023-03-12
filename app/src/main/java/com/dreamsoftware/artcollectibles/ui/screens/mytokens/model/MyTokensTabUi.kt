package com.dreamsoftware.artcollectibles.ui.screens.mytokens.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MyTokensTabUi(
    val type: MyTokensTabsTypeEnum,
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    var isSelected: Boolean = false
)
