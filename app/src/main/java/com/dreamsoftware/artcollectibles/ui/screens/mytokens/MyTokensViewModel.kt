package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabUi
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabsTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTokensViewModel @Inject constructor() : SupportViewModel<MyTokensUiState>() {

    override fun onGetDefaultState(): MyTokensUiState =
        MyTokensUiState(
            tabs = listOf(
                MyTokensTabUi(
                    type = MyTokensTabsTypeEnum.TOKENS_OWNED,
                    titleRes = R.string.my_tokens_tab_tokens_owned_text,
                    isSelected = true
                ),
                MyTokensTabUi(
                    type = MyTokensTabsTypeEnum.TOKENS_CREATED,
                    titleRes = R.string.my_tokens_tab_tokens_created_text
                )
            )
        )

    fun onNewTabSelected(newTabSelectedType: MyTokensTabsTypeEnum) {
        updateState {
            it.copy(
                tabs = it.tabs.map { tab ->
                    tab.copy(isSelected = tab.type == newTabSelectedType)
                },
                isLoading = true
            )
        }
    }
}

data class MyTokensUiState(
    val tabs: List<MyTokensTabUi> = emptyList(),
    val isLoading: Boolean = false
)