package com.dreamsoftware.artcollectibles.utils.notification.ui.impl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dreamsoftware.artcollectibles.ui.theme.Purple200
import com.dreamsoftware.artcollectibles.utils.notification.ui.IUINotificationHelper

class UINotificationHelperImpl(private val context: Context): IUINotificationHelper {

    enum class NotificationChannels {
        ALERTS_CHANNEL, COMMON_CHANNEL
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (notificationChannels in NotificationChannels.values())
                createNotificationChannel(
                    notificationChannels
                )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationChannels: NotificationChannels) {
        val channelName: CharSequence = notificationChannels.name
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(
            notificationChannels.name,
            channelName,
            importance
        )
        with(NotificationManagerCompat.from(context)) {
            createNotificationChannel(notificationChannel)
        }

    }


    /**
     * Show Notification
     * @param id
     * @param notification
     */
    private fun showNotification(id: Int, notification: Notification) {
        with(NotificationManagerCompat.from(context)) {
            notify(id, notification)
        }
    }

    override fun showImportantNotification(smallIcon: Int, id: Int, title: String, body: String, intent: Intent?) {
        showNotification(id, createImportantNotification(smallIcon, title, body, intent))
    }

    override fun createImportantNotification(
        smallIcon: Int,
        title: String,
        body: String,
        intent: Intent?
    ): Notification {

        val builder = NotificationCompat.Builder(context, NotificationChannels.ALERTS_CHANNEL.name)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setColorized(true)
            .setColor(Purple200.toArgb())
            .setStyle(NotificationCompat.BigTextStyle().bigText( body ))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)

        if (intent != null) {
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(pendingIntent)
        }

        return builder.build()
    }


    companion object {
        private const val DEFAULT_NOTIFICATION_ID = 1000
    }
}