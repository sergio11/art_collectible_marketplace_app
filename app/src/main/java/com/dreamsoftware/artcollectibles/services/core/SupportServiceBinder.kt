package com.dreamsoftware.artcollectibles.services.core

import android.os.Binder
import com.dreamsoftware.artcollectibles.services.INotificationService

abstract class SupportServiceBinder<T>: Binder() {
    abstract fun provideService(): T
}

typealias NotificationBackgroundServiceBinder = SupportServiceBinder<INotificationService>