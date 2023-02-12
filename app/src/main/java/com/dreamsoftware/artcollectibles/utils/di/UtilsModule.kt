package com.dreamsoftware.artcollectibles.utils.di

import android.content.Context
import android.content.Intent
import com.dreamsoftware.artcollectibles.services.di.qualifier.NotificationBackgroundServiceIntent
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import com.dreamsoftware.artcollectibles.utils.PasswordUtils
import com.dreamsoftware.artcollectibles.utils.notification.manager.INotificationManager
import com.dreamsoftware.artcollectibles.utils.notification.ui.IUINotificationHelper
import com.dreamsoftware.artcollectibles.utils.notification.manager.impl.NotificationManagerImpl
import com.dreamsoftware.artcollectibles.utils.notification.ui.impl.UINotificationHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    /**
     * Provide Crypto Utils
     */
    @Singleton
    @Provides
    fun provideCryptoUtils() = CryptoUtils()

    /**
     * Provide Secret Utils
     */
    @Singleton
    @Provides
    fun provideSecretUtils() = PasswordUtils()

    /**
     * Provide Application Aware
     */
    @Singleton
    @Provides
    fun provideApplicationAware(@ApplicationContext context: Context): IApplicationAware =
        context as IApplicationAware

    /**
     * Provide UI Notification Helper
     */
    @Singleton
    @Provides
    fun provideUINotificationHelper(@ApplicationContext context: Context): IUINotificationHelper =
        UINotificationHelperImpl(context)

    /**
     * Provide Notification Manager
     * @param notificationBackgroundServiceIntent
     */
    @Singleton
    @Provides
    fun provideNotificationManager(
        @NotificationBackgroundServiceIntent notificationBackgroundServiceIntent: Intent
    ): INotificationManager =
        NotificationManagerImpl(notificationBackgroundServiceIntent)
}