package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.model.NotificationDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveNotificationDTO

interface INotificationsDataSource {

    /**
     * Save notification
     * @param notification
     */
    @Throws(SaveNotificationException::class)
    suspend fun save(notification: SaveNotificationDTO)

    /**
     * Count notifications
     * @param userUid
     */
    @Throws(CountNotificationsException::class)
    suspend fun count(userUid: String): Long

    /**
     * Delete notification
     * @param userUid
     * @param notificationUid
     */
    @Throws(DeleteNotificationException::class)
    suspend fun delete(userUid: String, notificationUid: String)

    /**
     * Get notification by user
     * @param userUid
     */
    @Throws(GetNotificationsByUserException::class)
    suspend fun getNotificationByUser(userUid: String): Iterable<NotificationDTO>

    /**
     * Get notification by id exception
     * @param uid
     */
    @Throws(GetNotificationByIdException::class)
    suspend fun getNotificationById(uid: String): NotificationDTO
}