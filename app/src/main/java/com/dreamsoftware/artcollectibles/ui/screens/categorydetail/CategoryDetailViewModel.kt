package com.dreamsoftware.artcollectibles.ui.screens.categorydetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetArtCollectiblesByCategoryUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAvailableMarketItemsByCategoryUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCategoryDetailUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val getAvailableMarketItemsByCategoryUseCase: GetAvailableMarketItemsByCategoryUseCase,
    private val getArtCollectiblesByCategoryUseCase: GetArtCollectiblesByCategoryUseCase
) : SupportViewModel<CategoryDetailUiState>() {

    override fun onGetDefaultState(): CategoryDetailUiState = CategoryDetailUiState()


    fun loadAvailableMarketItemsByCategory(uid: String) {
        onLoading()
        fetchCategoryDetail(uid)
        fetchAvailableMarketItemsByCategory(uid)
    }

    fun loadArtCollectiblesByCategory(uid: String) {
        onLoading()
        fetchCategoryDetail(uid)
        fetchArtCollectiblesByCategory(uid)
    }

    private fun onCategoryDetailLoaded(category: ArtCollectibleCategory) {
        updateState {
            it.copy(category = category)
        }
    }

    private fun onSuccess(items: Iterable<Any>) {
        updateState {
            it.copy(
                isLoading = false,
                items = items
            )
        }
    }


    private fun fetchAvailableMarketItemsByCategory(uid: String) {
        getAvailableMarketItemsByCategoryUseCase.invoke(
            scope = viewModelScope,
            params = GetAvailableMarketItemsByCategoryUseCase.Params(categoryUid = uid),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun fetchArtCollectiblesByCategory(uid: String) {
        getArtCollectiblesByCategoryUseCase.invoke(
            scope = viewModelScope,
            params = GetArtCollectiblesByCategoryUseCase.Params(categoryUid = uid),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun fetchCategoryDetail(uid: String) {
        getCategoryDetailUseCase.invoke(
            scope = viewModelScope,
            params = GetCategoryDetailUseCase.Params(uid = uid),
            onSuccess = ::onCategoryDetailLoaded,
            onError = ::onErrorOccurred
        )
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState { it.copy(isLoading = false) }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }
}

data class CategoryDetailUiState(
    val isLoading: Boolean = false,
    val category: ArtCollectibleCategory? = null,
    val items: Iterable<Any> = emptyList()
)