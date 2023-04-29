package com.dreamsoftware.artcollectibles.ui.screens.notifications

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.Notification
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetMyNotificationsUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Notifications View Model
 */
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getMyNotificationsUseCase: GetMyNotificationsUseCase
) : SupportViewModel<NotificationsUiState>() {

    override fun onGetDefaultState(): NotificationsUiState = NotificationsUiState()

    fun load() {
        onLoading()
        getMyNotificationsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onSuccess,
            onError = ::onErrorOccurred
        )
    }

    private fun onSuccess(notifications: Iterable<Notification>) {
        updateState {
            it.copy(
                isLoading = false,
                notifications = notifications
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

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: Iterable<Notification> = emptyList(),
    var error: Exception? = null
)