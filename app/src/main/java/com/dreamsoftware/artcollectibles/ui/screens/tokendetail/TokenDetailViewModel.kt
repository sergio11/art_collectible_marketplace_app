package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.Comment
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
    private val isTokenAddedForSaleUseCase: IsTokenAddedForSaleUseCase,
    private val addTokenTokenToFavoritesUseCase: AddTokenToFavoritesUseCase,
    private val removeTokenFromFavoritesUseCase: RemoveTokenFromFavoritesUseCase,
    private val registerVisitorUseCase: RegisterVisitorUseCase,
    private val saveCommentUseCase: SaveCommentUseCase,
    private val getLastCommentsByTokenUseCase: GetLastCommentsByTokenUseCase
) : SupportViewModel<TokenDetailUiState>() {

    private companion object {
        const val COMMENTS_BY_TOKEN_LIMIT = 4
    }

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

    fun addTokenToFavorites(tokenId: BigInteger) {
        with(uiState.value) {
            authUserInfo?.let {
                addTokenTokenToFavoritesUseCase.invoke(
                    scope = viewModelScope,
                    params = AddTokenToFavoritesUseCase.Params(
                        tokenId = tokenId.toString(),
                        userAddress = it.walletAddress
                    ),
                    onSuccess = {
                        onTokenAddedToFavorites()
                    },
                    onError = ::onErrorOccurred
                )
            }
        }
    }

    fun removeTokenFromFavorites(tokenId: BigInteger) {
        with(uiState.value) {
            authUserInfo?.let {
                removeTokenFromFavoritesUseCase.invoke(
                    scope = viewModelScope,
                    params = RemoveTokenFromFavoritesUseCase.Params(
                        tokenId = tokenId.toString(),
                        userAddress = it.walletAddress
                    ),
                    onSuccess = {
                        onTokenRemovedFromFavorites()
                    },
                    onError = ::onErrorOccurred
                )
            }
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

    fun onPublishComment(comment: String) {
        with(uiState.value) {
            artCollectible?.let { token ->
                authUserInfo?.let { userInfo ->
                    saveCommentUseCase.invoke(
                        scope = viewModelScope,
                        params = SaveCommentUseCase.Params(
                            comment = comment,
                            userUid = userInfo.uid,
                            tokenId = token.id
                        ),
                        onSuccess = ::onCommentPublished,
                        onError = ::onErrorOccurred
                    )
                }
            }
        }
    }

    /**
     * Private Methods
     */

    private fun loadAllDataForToken(tokenId: BigInteger) {
        viewModelScope.launch {
            try {
                val artCollectible = loadTokenDetail(tokenId)
                val isTokenAddedForSale = isTokenAddedForSale(tokenId)
                val authUser = loadAuthUserDetail()
                val isTokenOwner = artCollectible.owner.uid == authUser.uid
                val isTokenCreator = artCollectible.author.uid == authUser.uid
                val lastComments = getLastCommentsByToken(tokenId, COMMENTS_BY_TOKEN_LIMIT)
                updateState {
                    it.copy(
                        artCollectible = artCollectible,
                        authUserInfo = authUser,
                        isLoading = false,
                        isTokenOwner = isTokenOwner,
                        isTokenAddedForSale = isTokenAddedForSale,
                        tokenAddedToFavorites = artCollectible.hasAddedToFav,
                        lastComments = lastComments
                    )
                }
                if (!isTokenOwner && !isTokenCreator) {
                    registerVisitor(tokenId, authUser.walletAddress)
                }
            } catch (ex: Exception) {
                onErrorOccurred(ex)
            }
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
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

    private fun onCommentPublished(comment: Comment) {
        viewModelScope.launch {
            val lastComments = getLastCommentsByToken(comment.tokenId, COMMENTS_BY_TOKEN_LIMIT)
            updateState {
                it.copy(
                    artCollectible = it.artCollectible?.let { token ->
                        token.copy(commentsCount = token.commentsCount + 1)
                    },
                    lastComments = lastComments
                )
            }
        }
    }

    private fun onTokenAddedToFavorites() {
        updateState {
            it.copy(
                tokenAddedToFavorites = true
            )
        }
    }

    private fun onTokenRemovedFromFavorites() {
        updateState {
            it.copy(
                tokenAddedToFavorites = false
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
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

    private suspend fun loadTokenDetail(tokenId: BigInteger) = getTokenDetailUseCase.invoke(
        scope = viewModelScope, params = GetTokenDetailUseCase.Params(tokenId)
    )

    private suspend fun getLastCommentsByToken(tokenId: BigInteger, limit: Int) =
        getLastCommentsByTokenUseCase.invoke(
            scope = viewModelScope,
            params = GetLastCommentsByTokenUseCase.Params(
                tokenId = tokenId.toString(),
                limit = limit
            )
        )

    private suspend fun loadAuthUserDetail() = getAuthUserProfileUseCase.invoke(
        scope = viewModelScope
    )

    private suspend fun isTokenAddedForSale(tokenId: BigInteger) =
        isTokenAddedForSaleUseCase.invoke(
            scope = viewModelScope, params = IsTokenAddedForSaleUseCase.Params(tokenId)
        )

    private fun registerVisitor(tokenId: BigInteger, userAddress: String) {
        registerVisitorUseCase.invoke(
            scope = viewModelScope,
            params = RegisterVisitorUseCase.Params(
                tokenId = tokenId.toString(),
                userAddress = userAddress
            ),
            onSuccess = {
                // ignore success
            },
            onError = {
                // ignore error
            }
        )
    }
}

data class TokenDetailUiState(
    var artCollectible: ArtCollectible? = null,
    var authUserInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val isBurned: Boolean = false,
    val isTokenOwner: Boolean = false,
    val isTokenAddedForSale: Boolean = false,
    val tokenPrice: String? = null,
    val tokenAddedToFavorites: Boolean = false,
    val lastComments: Iterable<Comment> = emptyList(),
    val isPutTokenForSaleConfirmButtonEnabled: Boolean = false,
    val isConfirmBurnTokenDialogVisible: Boolean = false,
    val isConfirmWithDrawFromSaleDialogVisible: Boolean = false,
    val isConfirmPutForSaleDialogVisible: Boolean = false
)