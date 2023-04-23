package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.INotificationsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.dreamsoftware.artcollectibles.data.firebase.mapper.NotificationMapper
import com.dreamsoftware.artcollectibles.data.firebase.mapper.SaveNotificationMapper
import com.dreamsoftware.artcollectibles.data.firebase.model.NotificationDTO
import com.dreamsoftware.artcollectibles.data.firebase.model.SaveNotificationDTO
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

internal class NotificationsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore,
    private val saveNotificationMapper: SaveNotificationMapper,
    private val notificationMapper: NotificationMapper
): INotificationsDataSource {

    private companion object {
        const val COLLECTION_NAME = "notifications"
        const val NOTIFICATIONS_FIELD_NAME = "notifications"
        const val NOTIFICATIONS_COUNT_SUFFIX = "_count"
        const val COUNT_FIELD_NAME = "count"
    }

    @Throws(SaveNotificationException::class)
    override suspend fun save(notification: SaveNotificationDTO): Unit = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME).apply {
                document(notification.uid)
                    .set(saveNotificationMapper.mapInToOut(notification), SetOptions.merge())
                    .await()
                document(notification.userUid)
                    .set(hashMapOf(NOTIFICATIONS_FIELD_NAME to FieldValue.arrayUnion(notification.uid)), SetOptions.merge())
                    .await()
                document(notification.userUid + NOTIFICATIONS_COUNT_SUFFIX).set(
                    hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)),
                    SetOptions.merge()
                ).await()
            }
        } catch (ex: Exception) {
            throw SaveNotificationException(
                "An error occurred when trying to save notification information",
                ex
            )
        }
    }

    @Throws(CountNotificationsException::class)
    override suspend fun count(userUid: String): Long = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userUid + NOTIFICATIONS_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: Exception) {
            throw CountNotificationsException(
                "An error occurred when trying to count notifications",
                ex
            )
        }
    }

    @Throws(DeleteNotificationException::class)
    override suspend fun delete(userUid: String, notificationUid: String): Unit = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME).apply {
                document(notificationUid).delete().await()
                document(userUid)
                    .set(hashMapOf(NOTIFICATIONS_FIELD_NAME to FieldValue.arrayRemove(notificationUid)), SetOptions.merge())
                    .await()
                document(userUid + NOTIFICATIONS_COUNT_SUFFIX).set(
                    hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)),
                    SetOptions.merge()
                ).await()
            }
        } catch (ex: Exception) {
            throw DeleteNotificationException(
                "An error occurred when trying to delete notification",
                ex
            )
        }
    }

    @Throws(GetNotificationsByUserException::class)
    override suspend fun getNotificationByUser(userUid: String): Iterable<NotificationDTO> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userUid)
                .get()
                .await()?.data?.let { it[NOTIFICATIONS_FIELD_NAME] as? Iterable<String> }
                ?.let { getNotificationsByIds(it) }
                ?: throw GetNotificationsByUserException("No notifications found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetNotificationsByUserException(
                "An error occurred when trying to get notifications",
                ex
            )
        }
    }

    @Throws(GetNotificationByIdException::class)
    override suspend fun getNotificationById(uid: String): NotificationDTO = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(uid)
                .get()
                .await()?.data?.let { notificationMapper.mapInToOut(it) }
                ?: throw GetNotificationByIdException("No notification found")
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetNotificationByIdException(
                "An error occurred when trying to get notification by id",
                ex
            )
        }
    }

    private suspend fun getNotificationsByIds(uidList: Iterable<String>): Iterable<NotificationDTO> =
        firebaseStore.collection(COLLECTION_NAME)
            .whereIn(FieldPath.documentId(), uidList.toList())
            .get().await()
            .documents.mapNotNull { it.data }
            .map { notificationMapper.mapInToOut(it) }.sortedByDescending { it.createdAt }
}