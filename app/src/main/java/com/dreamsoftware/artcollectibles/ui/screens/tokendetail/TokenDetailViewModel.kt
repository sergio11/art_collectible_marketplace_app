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
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
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
        burnTokenUseCase.invoke(
            scope = viewModelScope,
            params = BurnTokenUseCase.Params(tokenId),
            onBeforeStart = {
                updateState {
                    it.copy(
                        isLoading = true,
                        isConfirmBurnTokenDialogVisible = false
                    )
                }
            },
            onSuccess = {
                onTokenBurned()
            },
            onError = ::onErrorOccurred
        )
    }

    fun withDrawFromSale(tokenId: BigInteger) {
        withdrawFromSaleUseCase.invoke(
            scope = viewModelScope,
            params = WithdrawFromSaleUseCase.Params(tokenId),
            onBeforeStart = {
                updateState {
                    it.copy(
                        isLoading = true,
                        isConfirmWithDrawFromSaleDialogVisible = false
                    )
                }
            },
            onSuccess = {
                onTokenWithdrawnFromSale()
            },
            onError = ::onErrorOccurred
        )
    }

    fun putItemForSale(tokenId: BigInteger) {
        with(uiState.value) {
            tokenPrice?.toFloatOrNull()?.let { price ->
                putItemForSaleUseCase.invoke(
                    scope = viewModelScope,
                    params = PutItemForSaleUseCase.Params(tokenId, price),
                    onBeforeStart = {
                        updateState {
                            it.copy(
                                isLoading = true,
                                isConfirmPutForSaleDialogVisible = false
                            )
                        }
                    },
                    onSuccess = {
                        onTokenPutOnSale()
                    },
                    onError = ::onErrorOccurred
                )
            }
        }
    }

    fun onTokenPriceChanged(newPrice: String) {
        updateState {
            it.copy(
                tokenPrice = newPrice,
                isPutTokenForSaleConfirmButtonEnabled = newPrice.isNotBlank()
            )
        }
    }

    fun onConfirmBurnTokenDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isConfirmBurnTokenDialogVisible = isVisible) }
    }

    fun onConfirmWithDrawFromSaleDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isConfirmWithDrawFromSaleDialogVisible = isVisible) }
    }

    fun onConfirmPutForSaleDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isConfirmPutForSaleDialogVisible = isVisible) }
    }

    private suspend fun loadTokenDetail(tokenId: BigInteger) = getTokenDetailUseCase.invoke(
        scope = viewModelScope, params = GetTokenDetailUseCase.Params(tokenId)
    )

    private suspend fun loadAuthUserDetail() = getAuthUserProfileUseCase.invoke(
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
                isBurned = true,
                isConfirmBurnTokenDialogVisible = false
            )
        }
    }

    private fun onTokenPutOnSale() {
        updateState {
            it.copy(
                isLoading = false,
                isTokenAddedForSale = true,
                isConfirmPutForSaleDialogVisible = false,
                isPutTokenForSaleConfirmButtonEnabled = false,
                tokenPrice = null
            )
        }
    }

    private fun onTokenWithdrawnFromSale() {
        updateState {
            it.copy(
                isLoading = false,
                isTokenAddedForSale = false,
                isConfirmWithDrawFromSaleDialogVisible = false
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(
                isLoading = false,
                isConfirmBurnTokenDialogVisible = false,
                isConfirmWithDrawFromSaleDialogVisible = false,
                isConfirmPutForSaleDialogVisible = false,
                isPutTokenForSaleConfirmButtonEnabled = false,
                tokenPrice = null
            )
        }
    }
}

data class TokenDetailUiState(
    var artCollectible: ArtCollectible? = null,
    val isLoading: Boolean = false,
    val isBurned: Boolean = false,
    val isTokenOwner: Boolean = false,
    val isTokenAddedForSale: Boolean = false,
    val tokenPrice: String? = null,
    val isPutTokenForSaleConfirmButtonEnabled: Boolean = false,
    val isConfirmBurnTokenDialogVisible: Boolean = false,
    val isConfirmWithDrawFromSaleDialogVisible: Boolean = false,
    val isConfirmPutForSaleDialogVisible: Boolean = false
)