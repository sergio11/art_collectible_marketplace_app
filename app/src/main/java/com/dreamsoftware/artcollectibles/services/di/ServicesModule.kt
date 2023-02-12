package com.dreamsoftware.artcollectibles.services.di

import android.content.Context
import com.dreamsoftware.artcollectibles.services.NotificationBackgroundService
import com.dreamsoftware.artcollectibles.services.di.qualifier.NotificationBackgroundServiceIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Singleton
    @Provides
    @NotificationBackgroundServiceIntent
    fun provideNotificationBackgroundServiceIntent(@ApplicationContext context: Context) =
        NotificationBackgroundService.getServiceIntent(context)
}