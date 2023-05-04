package com.dreamsoftware.artcollectibles.ui.screens.discovery

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetArtCollectibleCategoriesUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.SearchUsersUseCase
import com.dreamsoftware.artcollectibles.ui.extensions.tabSelectedTypeOrDefault
import com.dreamsoftware.artcollectibles.ui.model.DiscoveryTabsTypeEnum
import com.dreamsoftware.artcollectibles.ui.model.TabUi
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Discovery View Model
 * @param searchUsersUseCase
 */
@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getArtCollectibleCategoriesUseCase: GetArtCollectibleCategoriesUseCase
) : SupportViewModel<DiscoveryUiState>() {

    override fun onGetDefaultState(): DiscoveryUiState = DiscoveryUiState(
        tabs = listOf(
            TabUi(
                type = DiscoveryTabsTypeEnum.DIGITAL_ARTISTS,
                iconRes = R.drawable.discover_artists_tab,
                titleRes = R.string.discovery_main_digital_artists_text,
                isSelected = true
            ),
            TabUi(
                type = DiscoveryTabsTypeEnum.NFT_CATEGORIES,
                iconRes = R.drawable.discover_nft_tab,
                titleRes = R.string.discovery_main_nft_categories_text
            )
        )
    )

    fun load() {
        onLoading()
        when (uiState.value.tabs.tabSelectedTypeOrDefault(default = DiscoveryTabsTypeEnum.DIGITAL_ARTISTS)) {
            DiscoveryTabsTypeEnum.DIGITAL_ARTISTS -> searchUsers()
            DiscoveryTabsTypeEnum.NFT_CATEGORIES -> fetchArtCollectibleCategories()
        }
    }

    fun onNewTabSelected(newTabSelectedType: DiscoveryTabsTypeEnum) {
        updateState {
            it.copy(
                tabs = it.tabs.map { tab ->
                    tab.copy(isSelected = tab.type == newTabSelectedType)
                }
            )
        }
        load()
    }

    fun onTermChanged(newTerm: String) {
        updateState {
            it.copy(
                searchTerm = newTerm,
                isLoading = true
            )
        }
        searchUsers()
    }

    fun onResetSearch() {
        updateState {
            it.copy(
                searchTerm = null,
                isLoading = true
            )
        }
        searchUsers()
    }

    private fun searchUsers() {
        with(uiState.value) {
            searchUsersUseCase.invoke(
                scope = viewModelScope,
                params = SearchUsersUseCase.Params(term = searchTerm),
                onSuccess = ::onSearchFinished,
                onError = ::onErrorOccurred
            )
        }
    }

    private fun fetchArtCollectibleCategories() {
        getArtCollectibleCategoriesUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onFetchArtCollectibleCategoriesSuccess,
            onError = ::onErrorOccurred
        )
    }


    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onSearchFinished(userResult: Iterable<UserInfo>) {
        updateState {
            it.copy(
                items = userResult,
                isLoading = false
            )
        }
    }

    private fun onFetchArtCollectibleCategoriesSuccess(categories: Iterable<ArtCollectibleCategory>) {
        updateState {
            it.copy(
                items = categories,
                isLoading = false
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(
                items = emptyList(),
                isLoading = false,
                error = ex
            )
        }
    }
}

data class DiscoveryUiState(
    val tabs: List<TabUi<DiscoveryTabsTypeEnum>> = emptyList(),
    val searchTerm: String? = null,
    val items: Iterable<Any> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)