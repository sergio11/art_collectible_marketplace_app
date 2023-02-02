package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetMyTokensCreatedUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetMyTokensOwnedUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabUi
import com.dreamsoftware.artcollectibles.ui.screens.mytokens.model.MyTokensTabsTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTokensViewModel @Inject constructor(
    private val getMyTokensCreatedUseCase: GetMyTokensCreatedUseCase,
    private val getMyTokensOwnedUseCase: GetMyTokensOwnedUseCase
) : SupportViewModel<MyTokensUiState>() {

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
                }
            )
        }
        loadTokens()
    }

    fun loadTokens() {
        onLoading()
        getSelectedTabType()?.let {
            if(it == MyTokensTabsTypeEnum.TOKENS_OWNED) {
                loadMyTokensOwned()
            } else {
                loadMyTokensCreated()
            }
        } ?: loadMyTokensOwned()
    }

    private fun loadMyTokensCreated() {
        with(uiState.value) {
            getMyTokensCreatedUseCase.invoke(
                scope = viewModelScope,
                onSuccess = ::onLoadTokensCompleted,
                onError = ::onErrorOccurred
            )
        }
    }

    private fun loadMyTokensOwned() {
        with(uiState.value) {
            getMyTokensOwnedUseCase.invoke(
                scope = viewModelScope,
                onSuccess = ::onLoadTokensCompleted,
                onError = ::onErrorOccurred
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onLoadTokensCompleted(tokenList: Iterable<ArtCollectible>) {
        updateState {
            it.copy(
                tokens = tokenList.toList(),
                isLoading = false
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
        Log.d("ART_COLL", "onErrorOccurred ${ex.message} CALLED!")
        updateState { it.copy(isLoading = false) }
    }

    private fun getSelectedTabType(): MyTokensTabsTypeEnum? =
        uiState.value.tabs.find { it.isSelected }?.type
}

data class MyTokensUiState(
    val tabs: List<MyTokensTabUi> = emptyList(),
    val isLoading: Boolean = false,
    val tokens: List<ArtCollectible> = emptyList()
)