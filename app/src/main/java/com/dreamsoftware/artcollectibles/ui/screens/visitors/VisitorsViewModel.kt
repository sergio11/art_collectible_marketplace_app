package com.dreamsoftware.artcollectibles.ui.screens.visitors

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetVisitorsByTokenUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class VisitorsViewModel @Inject constructor(
    private val getVisitorsByTokenUseCase: GetVisitorsByTokenUseCase
) : SupportViewModel<VisitorsUiState>() {

    override fun onGetDefaultState(): VisitorsUiState = VisitorsUiState()

    fun load(tokenId: BigInteger) {
        onLoading()
        getVisitorsByTokenUseCase.invoke(
            scope = viewModelScope,
            params = GetVisitorsByTokenUseCase.Params(tokenId),
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(userResult: Iterable<UserInfo>) {
        updateState {
            it.copy(
                isLoading = false,
                userResult = userResult
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
        updateState {
            it.copy(
                isLoading = false,
                error = ex
            )
        }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }
}

data class VisitorsUiState(
    val isLoading: Boolean = false,
    val userResult: Iterable<UserInfo> = emptyList(),
    var error: Exception? = null
)