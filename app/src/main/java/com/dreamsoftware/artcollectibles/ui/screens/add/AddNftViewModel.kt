package com.dreamsoftware.artcollectibles.ui.screens.add

import android.net.Uri
import android.util.Log
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

    companion object {
        const val MIN_NFT_NAME_LENGTH = 6
        const val MIN_NFT_DESCRIPTION_LENGTH = 10
    }

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
        updateState {
            it.copy(
                name = name,
                isCreateButtonEnabled = createButtonShouldBeEnabled(
                    name = name,
                    description = it.description.orEmpty()
                )
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        updateState {
            it.copy(
                description = description,
                isCreateButtonEnabled = createButtonShouldBeEnabled(name = it.name, description)
            )
        }
    }

    fun onRoyaltyChanged(newRoyalty: Float) {
        updateState {
            it.copy(
                royalty = newRoyalty
            )
        }
    }

    fun onAddNewTag(newTag: String) {
        updateState {
            it.copy(
                tags = buildList {
                    addAll(it.tags)
                    add(newTag)
                }
            )
        }
    }

    fun getFileProviderAuthority() = applicationAware.getFileProviderAuthority()

    fun onCreate() {
        with(uiState.value) {
            onLoading()
            createArtCollectibleUseCase.invoke(
                scope = viewModelScope,
                params = CreateArtCollectibleUseCase.Params(
                    imagePath = imageUri.toString(),
                    mediaType = mimeType,
                    name = name,
                    description = description,
                    royalty = royalty.toLong(),
                    tags = tags
                ),
                onSuccess = ::onCreateSuccess,
                onError = ::onCreateError
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onCreateSuccess(artCollectible: ArtCollectible) {
        updateState {
            it.copy(
                isLoading = false,
                isTokenMinted = true
            )
        }
        Log.d("ART_COLL", "onCreateSuccess id: ${artCollectible.id}")
    }

    private fun onCreateError(ex: Exception) {
        updateState { it.copy(isLoading = false) }
        ex.printStackTrace()
        Log.d("ART_COLL", "onCreateError EX: ${ex.message}")
    }

    private fun createButtonShouldBeEnabled(name: String, description: String) =
        description.length > MIN_NFT_DESCRIPTION_LENGTH
                && name.length > MIN_NFT_NAME_LENGTH

}

data class AddNftUiState(
    val isLoading: Boolean = false,
    val imageUri: Uri? = null,
    val mimeType: String = "",
    val name: String = "",
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val royalty: Float = 0f,
    val isCreateButtonEnabled: Boolean = false,
    val isTokenMinted: Boolean = false
)