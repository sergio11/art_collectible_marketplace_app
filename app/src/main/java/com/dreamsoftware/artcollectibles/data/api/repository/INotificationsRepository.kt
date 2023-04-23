package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.domain.models.CreateNotification
import com.dreamsoftware.artcollectibles.domain.models.Notification
import java.math.BigInteger

interface INotificationsRepository {

    @Throws(SaveNotificationDataException::class)
    suspend fun save(notification: CreateNotification): Notification

    @Throws(DeleteNotificationDataException::class)
    suspend fun delete(userUid: String, notificationUid: String)

    @Throws(CountNotificationsByUserDataException::class)
    suspend fun count(userUid: String): Long

    @Throws(GetNotificationsByUserDataException::class)
    suspend fun getNotificationsByUser(userUid: String): Iterable<Notification>

    @Throws(GetNotificationByIdDataException::class)
    suspend fun getNotificationById(uid: String): Notification
}