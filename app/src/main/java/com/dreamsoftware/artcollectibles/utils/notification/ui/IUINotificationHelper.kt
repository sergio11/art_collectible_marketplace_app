package com.dreamsoftware.artcollectibles.utils.notification.ui

import android.app.Notification
import android.content.Intent
import androidx.annotation.DrawableRes

interface IUINotificationHelper {

    /**
     * Show Important Notification
     * @param smallIcon
     * @param id
     * @param title
     * @param body
     * @param intent
     */
    fun showImportantNotification(
        @DrawableRes smallIcon: Int,
        id: Int,
        title: String,
        body: String,
        intent: Intent? = null
    )

    /**
     * Create Important Notification
     * @param smallIcon
     * @param title
     * @param body
     * @param intent
     * @return
     */
    fun createImportantNotification(
        @DrawableRes smallIcon: Int,
        title: String,
        body: String,
        intent: Intent? = null
    ): Notification
}