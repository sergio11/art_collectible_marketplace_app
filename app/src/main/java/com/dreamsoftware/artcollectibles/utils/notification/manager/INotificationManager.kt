package com.dreamsoftware.artcollectibles.utils.notification.manager

import android.content.Context

interface INotificationManager {
    fun bind(context: Context)
    fun unBind(context: Context)
}