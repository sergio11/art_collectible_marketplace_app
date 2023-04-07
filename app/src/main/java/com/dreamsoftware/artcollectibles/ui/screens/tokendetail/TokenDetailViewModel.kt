package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
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
    private val getLastCommentsByTokenUseCase: GetLastCommentsByTokenUseCase,
    private val getLastTokenMarketTransactionsUseCase: GetLastTokenMarketTransactionsUseCase,
    private val getSimilarTokensUseCase: GetSimilarTokensUseCase
) : SupportViewModel<TokenDetailUiState>() {

    private companion object {
        const val COMMENTS_BY_TOKEN_LIMIT = 4
        const val HISTORY_BY_TOKEN_LIMIT = 4
        const val SIMILAR_TOKENS_LIMIT = 6
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
            Log.d("ART_COLL", "tokenPriceInEth -> $tokenPriceInEth")
            tokenPriceInEth?.let { price ->
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

    fun onTokenPriceChanged(newPriceInEth: Float) {
        updateState {
            it.copy(
                tokenPriceInEth = newPriceInEth,
                isPutTokenForSaleConfirmButtonEnabled = newPriceInEth > 0
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
                val lastComments = if (artCollectible.commentsCount > 0) {
                    getLastCommentsByToken(tokenId, COMMENTS_BY_TOKEN_LIMIT)
                } else {
                    emptyList()
                }
                val lastMarketHistory = getLastTokenMarketTransactions(tokenId, HISTORY_BY_TOKEN_LIMIT)
                updateState {
                    it.copy(
                        artCollectible = artCollectible,
                        authUserInfo = authUser,
                        isLoading = false,
                        isTokenOwner = isTokenOwner,
                        isTokenAddedForSale = isTokenAddedForSale,
                        tokenAddedToFavorites = artCollectible.hasAddedToFav,
                        lastComments = lastComments,
                        lastMarketHistory = lastMarketHistory
                    )
                }
                loadSimilarTokens(artCollectible.metadata.cid)
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
                tokenPriceInEth = null
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
                tokenPriceInEth = null
            )
        }
    }

    private suspend fun loadTokenDetail(tokenId: BigInteger) = getTokenDetailUseCase.invoke(
        scope = viewModelScope, params = GetTokenDetailUseCase.Params(tokenId)
    )

    private suspend fun getLastTokenMarketTransactions(tokenId: BigInteger, limit: Int) =  runCatching {
        getLastTokenMarketTransactionsUseCase.invoke(
            scope = viewModelScope,
            params = GetLastTokenMarketTransactionsUseCase.Params(
                tokenId = tokenId,
                limit = limit
            )
        )
    }.getOrDefault(emptyList())


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

    private fun loadSimilarTokens(tokenCid: String) {
        getSimilarTokensUseCase.invoke(
            scope = viewModelScope,
            params = GetSimilarTokensUseCase.Params(
                cid = tokenCid,
                count = SIMILAR_TOKENS_LIMIT
            ),
            onSuccess = { similarTokens ->
                 updateState {
                     it.copy(similarTokens = similarTokens)
                 }
            },
            onError = {
                // ignore error
            }
        )
    }

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
    val tokenPriceInEth: Float? = null,
    val tokenAddedToFavorites: Boolean = false,
    val lastComments: Iterable<Comment> = emptyList(),
    val lastMarketHistory: Iterable<ArtCollectibleForSale> = emptyList(),
    val similarTokens: Iterable<ArtCollectible> = emptyList(),
    val isPutTokenForSaleConfirmButtonEnabled: Boolean = false,
    val isConfirmBurnTokenDialogVisible: Boolean = false,
    val isConfirmWithDrawFromSaleDialogVisible: Boolean = false,
    val isConfirmPutForSaleDialogVisible: Boolean = false
)