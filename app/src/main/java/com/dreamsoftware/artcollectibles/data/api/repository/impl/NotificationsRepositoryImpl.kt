package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.*
import com.dreamsoftware.artcollectibles.data.api.mapper.NotificationMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.SaveNotificationMapper
import com.dreamsoftware.artcollectibles.data.api.repository.INotificationsRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.data.firebase.datasource.INotificationsDataSource
import com.dreamsoftware.artcollectibles.domain.models.CreateNotification
import com.dreamsoftware.artcollectibles.domain.models.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class NotificationsRepositoryImpl(
    private val notificationDataSource: INotificationsDataSource,
    private val notificationMapper: NotificationMapper,
    private val saveNotificationMapper: SaveNotificationMapper,
    private val userRepository: IUserRepository
): INotificationsRepository {

    @Throws(SaveNotificationDataException::class)
    override suspend fun save(notification: CreateNotification): Notification = withContext(Dispatchers.IO) {
        try {
            saveNotificationMapper.mapInToOut(notification).let {
                with(notificationDataSource) {
                    save(it)
                    notificationMapper.mapInToOut(getNotificationById(it.uid).let {
                        NotificationMapper.InputData(
                            notificationDTO = it,
                            userInfoDTO = userRepository.get(it.userUid, false)
                        )
                    })
                }
            }
        } catch (ex: Exception) {
            throw SaveNotificationDataException("An error occurred when trying to save a notification")
        }
    }

    @Throws(DeleteNotificationDataException::class)
    override suspend fun delete(userUid: String, notificationUid: String) {
        withContext(Dispatchers.IO) {
            try {
                notificationDataSource.delete(userUid, notificationUid)
            } catch (ex: Exception) {
                throw DeleteCommentDataException("An error occurred when trying to delete notification", ex)
            }
        }
    }

    @Throws(CountNotificationsByUserDataException::class)
    override suspend fun count(userUid: String): Long = withContext(Dispatchers.IO) {
        try {
            notificationDataSource.count(userUid)
        } catch (ex: Exception) {
            throw CountNotificationsByUserDataException("An error occurred when trying to count notifications", ex)
        }
    }

    @Throws(GetNotificationsByUserDataException::class)
    override suspend fun getNotificationsByUser(userUid: String): Iterable<Notification> = withContext(Dispatchers.IO) {
        try {
            val user = userRepository.get(userUid, true)
            notificationMapper.mapInListToOutList(notificationDataSource.getNotificationByUser(userUid).map {
                NotificationMapper.InputData(
                    notificationDTO = it,
                    userInfoDTO = user
                )
            })
        } catch (ex: Exception) {
            throw GetNotificationsByUserDataException("An error occurred when trying to get notifications by user uid", ex)
        }
    }

    @Throws(GetNotificationByIdDataException::class)
    override suspend fun getNotificationById(uid: String): Notification = withContext(Dispatchers.IO) {
        try {
            notificationMapper.mapInToOut(notificationDataSource.getNotificationById(uid).let {
                NotificationMapper.InputData(
                    notificationDTO = it,
                    userInfoDTO = userRepository.get(it.userUid, true)
                )
            })
        } catch (ex: Exception) {
            throw GetNotificationByIdDataException("An error occurred when trying to get notification by id", ex)
        }
    }
}