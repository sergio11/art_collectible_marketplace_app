package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class TokenDetailViewModel @Inject constructor(
    private val getTokenDetailUseCase: GetTokenDetailUseCase,
    private val burnTokenUseCase: BurnTokenUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val putItemForSaleUseCase: PutItemForSaleUseCase,
    private val withdrawFromSaleUseCase: WithdrawFromSaleUseCase,
    private val isTokenAddedForSaleUseCase: IsTokenAddedForSaleUseCase
) : SupportViewModel<TokenDetailUiState>() {

    override fun onGetDefaultState(): TokenDetailUiState = TokenDetailUiState()

    fun loadDetail(tokenId: BigInteger) {
        onLoading()
        loadAllDataForToken(tokenId)
    }

    fun burnToken(tokenId: BigInteger) {
        onLoading()
        burnTokenUseCase.invoke(
            scope = viewModelScope,
            params = BurnTokenUseCase.Params(tokenId),
            onSuccess = {
                onTokenBurned()
            },
            onError = ::onErrorOccurred
        )
    }

    private suspend fun loadTokenDetail(tokenId: BigInteger) =
        getTokenDetailUseCase.invoke(
            scope = viewModelScope,
            params = GetTokenDetailUseCase.Params(tokenId)
        )

    private suspend fun loadAuthUserDetail() =
        getUserProfileUseCase.invoke(
            scope = viewModelScope
        )

    private fun loadAllDataForToken(tokenId: BigInteger) {
        viewModelScope.launch {
            try {
                val artCollectible = loadTokenDetail(tokenId)
                val authUser = loadAuthUserDetail()
                onLoadDetailCompleted(artCollectible, authUser)
            } catch (ex: Exception) {
                onErrorOccurred(ex)
            }
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onLoadDetailCompleted(artCollectible: ArtCollectible, userInfo: UserInfo) {
        updateState {
            it.copy(
                artCollectible = artCollectible,
                isLoading = false,
                isTokenOwner = artCollectible.author.uid == userInfo.uid
            )
        }
    }

    private fun onTokenBurned() {
        updateState {
            it.copy(
                artCollectible = null,
                isLoading = false,
                isBurned = true
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState { it.copy(isLoading = false) }
    }
}

data class TokenDetailUiState(
    var artCollectible: ArtCollectible? = null,
    val isLoading: Boolean = false,
    val isBurned: Boolean = false,
    val isTokenOwner: Boolean = false
)