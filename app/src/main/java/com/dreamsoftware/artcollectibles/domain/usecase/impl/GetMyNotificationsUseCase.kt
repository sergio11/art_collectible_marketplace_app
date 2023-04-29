package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.INotificationsRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.domain.models.Notification
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase

class GetMyNotificationsUseCase(
    private val preferenceRepository: IPreferenceRepository,
    private val notificationRepository: INotificationsRepository
): BaseUseCase<Iterable<Notification>>() {

    override suspend fun onExecuted(): Iterable<Notification> =
        notificationRepository.getNotificationsByUser(preferenceRepository.getAuthUserUid())
}