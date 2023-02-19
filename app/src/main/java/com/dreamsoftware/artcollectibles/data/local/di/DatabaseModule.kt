package com.dreamsoftware.artcollectibles.data.local.di

import android.content.Context
import androidx.room.Room
import com.dreamsoftware.artcollectibles.data.local.room.dao.metadata.ITokenMetadataDAO
import com.dreamsoftware.artcollectibles.data.local.room.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideMainDao(appDatabase: AppDatabase): ITokenMetadataDAO =
        appDatabase.tokenMetadataDAO()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
}
