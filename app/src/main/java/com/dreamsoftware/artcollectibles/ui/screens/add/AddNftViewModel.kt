package com.dreamsoftware.artcollectibles.ui.screens.add

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CreateArtCollectibleUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNftViewModel @Inject constructor(
    private val applicationAware: IApplicationAware,
    private val createArtCollectibleUseCase: CreateArtCollectibleUseCase
) : SupportViewModel<AddNftUiState>() {

    override fun onGetDefaultState(): AddNftUiState = AddNftUiState()

    fun onImageSelected(imageUri: Uri, mimeType: String) {
        updateState {
            it.copy(
                imageUri = imageUri,
                mimeType = mimeType
            )
        }
    }

    fun onResetImage() {
        updateState { it.copy(imageUri = null) }
    }

    fun onNameChanged(name: String) {
        updateState { it.copy(name = name) }
    }

    fun onDescriptionChanged(description: String) {
        updateState { it.copy(description = description) }
    }

    fun getFileProviderAuthority() = applicationAware.getFileProviderAuthority()

    fun create() {
        with(uiState.value) {
            viewModelScope.launch {
                onLoading()
                createArtCollectibleUseCase.invoke(
                    params = CreateArtCollectibleUseCase.Params(
                        imagePath = imageUri.toString(),
                        name = name,
                        description = description,
                        royalty = royalty
                    ),
                    onSuccess = ::onCreateSuccess,
                    onError = ::onCreateError
                )
            }
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onCreateSuccess(artCollectible: ArtCollectible) {
        updateState { it.copy(isLoading = false) }
    }

    private fun onCreateError(ex: Exception) {
        updateState { it.copy(isLoading = false) }
    }

}

data class AddNftUiState(
    val isLoading: Boolean = false,
    val imageUri: Uri? = null,
    val mimeType: String = "",
    val name: String = "",
    val description: String? = null,
    val royalty: Long = 0L
)