package com.dreamsoftware.artcollectibles.ui.screens.artistdetail

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FollowUserUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAuthUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UnfollowUserUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase
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

            }
        )
    }

    private fun loadAllDataForUser(uid: String) {
        viewModelScope.launch {
            try {
                val authUser = loadAuthUserDetail()
                val userInfo = getUserProfileUseCase.invoke(
                    scope = this,
                    params = GetUserProfileUseCase.Params(uid)
                )
                updateState {
                    it.copy(
                        isLoading = false,
                        isAuthUser = userInfo.uid == authUser.uid,
                        userInfo = userInfo
                    )
                }
            } catch (ex: Exception) {
                onErrorOccurred(ex)
            }
        }
    }

    private suspend fun loadAuthUserDetail() = getAuthUserProfileUseCase.invoke(
        scope = viewModelScope
    )

    private fun onFollowCompleted() {
        updateState { it.copy(isFollowing = true) }
    }

    private fun onUnfollowCompleted() {
        updateState { it.copy(isFollowing = false) }
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
    val isFollowing: Boolean = false
)