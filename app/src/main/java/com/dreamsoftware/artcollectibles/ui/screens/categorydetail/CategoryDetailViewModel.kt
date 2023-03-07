package com.dreamsoftware.artcollectibles.ui.screens.categorydetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCategoryDetailUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase
) : SupportViewModel<CategoryDetailUiState>() {

    override fun onGetDefaultState(): CategoryDetailUiState = CategoryDetailUiState()

    fun load(uid: String) {
        onLoading()
        getCategoryDetailUseCase.invoke(
            scope = viewModelScope,
            params = GetCategoryDetailUseCase.Params(uid = uid),
            onSuccess = ::onCategoryDetailLoaded,
            onError = ::onErrorOccurred
        )
    }

    private fun onCategoryDetailLoaded(category: ArtCollectibleCategory) {
        updateState {
            it.copy(
                isLoading = false,
                category = category
            )
        }
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
    val category: ArtCollectibleCategory? = null
)