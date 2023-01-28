package com.dreamsoftware.artcollectibles.ui.screens.mytokens.model

import androidx.annotation.StringRes

data class MyTokensTabUi(
    val type: MyTokensTabsTypeEnum,
    @StringRes val titleRes: Int,
    var isSelected: Boolean = false
)
