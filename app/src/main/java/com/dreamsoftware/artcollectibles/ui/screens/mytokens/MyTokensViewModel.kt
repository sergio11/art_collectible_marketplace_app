package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetMyFavoriteTokensUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetMyTokensCreatedUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetMyTokensOwnedUseCase
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTypeOrDefault
import com.dreamsoftware.artcollectibles.ui.model.MyTokensTabsTypeEnum
import com.dreamsoftware.artcollectibles.ui.model.TabUi
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTokensViewModel @Inject constructor(
    private val getMyTokensCreatedUseCase: GetMyTokensCreatedUseCase,
    private val getMyTokensOwnedUseCase: GetMyTokensOwnedUseCase,
    private val myTokensScreenErrorMapper: MyTokensScreenErrorMapper,
    private val getMyFavoriteTokensUseCase: GetMyFavoriteTokensUseCase
) : SupportViewModel<MyTokensUiState>() {

    override fun onGetDefaultState(): MyTokensUiState =
        MyTokensUiState(
            tabs = listOf(
                TabUi(
                    type = MyTokensTabsTypeEnum.TOKENS_OWNED,
                    iconRes = R.drawable.owned_tab_icon,
                    titleRes = R.string.my_tokens_tokens_owned_title,
                    isSelected = true
                ),
                TabUi(
                    type = MyTokensTabsTypeEnum.TOKENS_CREATED,
                    iconRes = R.drawable.created_tab_icon,
                    titleRes = R.string.my_tokens_tokens_created_title
                ),
                TabUi(
                    type = MyTokensTabsTypeEnum.TOKENS_LIKED,
                    iconRes = R.drawable.favorite_tab_icon,
                    titleRes = R.string.my_tokens_tokens_favorites_title
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
        when(uiState.value.tabs.tabSelectedTypeOrDefault(default = MyTokensTabsTypeEnum.TOKENS_OWNED)) {
            MyTokensTabsTypeEnum.TOKENS_OWNED -> loadMyTokensOwned()
            MyTokensTabsTypeEnum.TOKENS_CREATED -> loadMyTokensCreated()
            MyTokensTabsTypeEnum.TOKENS_LIKED -> loadMyFavoriteTokens()
        }
    }

    private fun loadMyTokensCreated() {
        getMyTokensCreatedUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onLoadTokensCompleted,
            onError = ::onErrorOccurred
        )
    }

    private fun loadMyTokensOwned() {
        getMyTokensOwnedUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onLoadTokensCompleted,
            onError = ::onErrorOccurred
        )
    }

    private fun loadMyFavoriteTokens() {
        getMyFavoriteTokensUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onLoadTokensCompleted,
            onError = ::onErrorOccurred
        )
    }

    private fun onLoading() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
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
        updateState {
            it.copy(
                tokens = emptyList(),
                isLoading = false,
                errorMessage = myTokensScreenErrorMapper.mapToMessage(ex)
            )
        }
    }
}

data class MyTokensUiState(
    val tabs: List<TabUi<MyTokensTabsTypeEnum>> = emptyList(),
    val isLoading: Boolean = false,
    val tokens: List<ArtCollectible> = emptyList(),
    val errorMessage: String? = null
)