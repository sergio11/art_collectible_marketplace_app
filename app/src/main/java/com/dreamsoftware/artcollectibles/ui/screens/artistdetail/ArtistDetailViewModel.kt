package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.*
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val checkAuthUserIsFollowingToUseCase: CheckAuthUserIsFollowingToUseCase,
    private val getTokensOwnedByUserUseCase: GetTokensOwnedByUserUseCase,
    private val getTokensCreatedByUserUseCase: GetTokensCreatedByUserUseCase
) : SupportViewModel<ArtistDetailUiState>() {

    override fun onGetDefaultState(): ArtistDetailUiState = ArtistDetailUiState()

    fun loadDetail(uid: String) {
        onLoading()
        loadAllDataForUser(uid)
    }

    fun followUser(uid: String) {
        followUserUseCase.invoke(
            scope = viewModelScope,
            params = FollowUserUseCase.Params(
                userUid = uid
            ),
            onSuccess = {
                onFollowCompleted()
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun unfollowUser(uid: String) {
        unfollowUserUseCase.invoke(
            scope = viewModelScope,
            params = UnfollowUserUseCase.Params(
                userUid = uid
            ),
            onSuccess = {
                onUnfollowCompleted()
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    private fun loadAllDataForUser(uid: String) {
        viewModelScope.launch {
            try {
                val authUserDeferred =  async { loadAuthUserDetail() }
                val loadProfileUseCaseDeferred = async { loadUserProfile(uid) }
                val checkAuthUserIsFollowingToDeferred = async { checkAuthUserIsFollowingTo(uid) }
                val authUser = authUserDeferred.await()
                val userInfo = loadProfileUseCaseDeferred.await()
                val isFollowingTo = checkAuthUserIsFollowingToDeferred.await()
                updateState {
                    it.copy(
                        isLoading = false,
                        isAuthUser = userInfo.uid == authUser.uid,
                        userInfo = userInfo,
                        isFollowing = isFollowingTo
                    )
                }
                loadTokensOwnedByUser(userAddress = userInfo.walletAddress)
                loadTokensCreatedByUser(userAddress = userInfo.walletAddress)
            } catch (ex: Exception) {
                onErrorOccurred(ex)
            }
        }
    }

    private suspend fun loadAuthUserDetail() = getAuthUserProfileUseCase.invoke(
        scope = viewModelScope
    )

    private suspend fun loadUserProfile(userUid: String) = getUserProfileUseCase.invoke(
        scope = viewModelScope,
        params = GetUserProfileUseCase.Params(userUid)
    )

    private suspend fun checkAuthUserIsFollowingTo(userUid: String) = checkAuthUserIsFollowingToUseCase.invoke(
        scope = viewModelScope,
        params = CheckAuthUserIsFollowingToUseCase.Params(userUid)
    )

    private fun loadTokensOwnedByUser(userAddress: String) {
        getTokensOwnedByUserUseCase.invoke(
            scope = viewModelScope,
            params = GetTokensOwnedByUserUseCase.Params(userAddress),
            onSuccess = ::onLoadTokensOwnedCompleted,
            onError =  ::onErrorOccurred
        )
    }

    private fun loadTokensCreatedByUser(userAddress: String) {
        getTokensCreatedByUserUseCase.invoke(
            scope = viewModelScope,
            params = GetTokensCreatedByUserUseCase.Params(userAddress),
            onSuccess = ::onLoadTokensCreatedCompleted,
            onError = ::onErrorOccurred
        )
    }

    private fun onFollowCompleted() {
        updateState { it.copy(isFollowing = true) }
    }

    private fun onUnfollowCompleted() {
        updateState { it.copy(isFollowing = false) }
    }

    private fun onLoadTokensOwnedCompleted(tokensOwned: Iterable<ArtCollectible>) {
        updateState { it.copy(tokensOwned = tokensOwned) }
    }

    private fun onLoadTokensCreatedCompleted(tokensCreated: Iterable<ArtCollectible>) {
        updateState { it.copy(tokensCreated = tokensCreated) }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(isLoading = false)
        }
    }
}

data class ArtistDetailUiState(
    val isLoading: Boolean = false,
    val isAuthUser: Boolean = false,
    val userInfo: UserInfo? = null,
    val isFollowing: Boolean = false,
    val tokensOwned: Iterable<ArtCollectible> = emptyList(),
    val tokensCreated: Iterable<ArtCollectible> = emptyList()
)