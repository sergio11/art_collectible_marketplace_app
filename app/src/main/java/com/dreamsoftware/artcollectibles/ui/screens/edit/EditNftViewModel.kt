package com.dreamsoftware.artcollectibles.ui.screens.edit

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchTokenMetadataUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UpdateTokenMetadataUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNftViewModel @Inject constructor(
    private val fetchTokenMetadataUseCase: FetchTokenMetadataUseCase,
    private val updateTokenMetadataUseCase: UpdateTokenMetadataUseCase
): SupportViewModel<EditNftUiState>() {

    override fun onGetDefaultState(): EditNftUiState = EditNftUiState()

    fun load(metadataCid: String) {
        onLoading()
        fetchTokenMetadataUseCase.invoke(
            scope = viewModelScope,
            params = FetchTokenMetadataUseCase.Params(metadataCid),
            onSuccess = ::onFetchTokenMetadataCompleted,
            onError = ::onErrorOccurred
        )
    }

    fun save() {
        with(uiState.value) {
            onLoading()
            updateTokenMetadataUseCase.invoke(
                scope = viewModelScope,
                params = UpdateTokenMetadataUseCase.Params(
                    cid = tokenCid.orEmpty(),
                    name = tokenName,
                    description = tokenDescription,
                    tags = tokenTags
                ),
                onSuccess = ::onFetchTokenMetadataCompleted,
                onError = ::onErrorOccurred
            )
        }
    }

    fun onNameChanged(name: String) {
        updateState {
            it.copy(tokenName = name)
        }
    }

    fun onDescriptionChanged(description: String) {
        updateState {
            it.copy(tokenDescription = description)
        }
    }

    fun onAddNewTag(newTag: String) {
        updateState {
            it.copy(
                tokenTags = buildList {
                    addAll(it.tokenTags)
                    add(newTag)
                }
            )
        }
    }

    fun onDeleteTag(tag: String) {
        updateState { state ->
            state.copy(tokenTags = buildList {
                state.tokenTags.filterNot { it == tag }.let(::addAll)
            })
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onFetchTokenMetadataCompleted(artCollectibleMetadata: ArtCollectibleMetadata) {
        with(artCollectibleMetadata) {
            updateState {
                it.copy(
                    isLoading = false,
                    tokenCid = cid,
                    tokenImageUrl = imageUrl,
                    tokenName = name,
                    tokenDescription = description,
                    tokenTags = tags
                )
            }
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState { it.copy(isLoading = false) }
        ex.printStackTrace()
    }
}

data class EditNftUiState(
    val isLoading: Boolean = false,
    val tokenCid: String? = null,
    val tokenImageUrl: String? = null,
    val tokenName: String = "",
    val tokenDescription: String? = null,
    val tokenTags: List<String> = emptyList()
)