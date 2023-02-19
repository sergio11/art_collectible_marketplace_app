package com.dreamsoftware.artcollectibles.data.database.di

import android.content.Context
import androidx.room.Room
import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.ITokenMetadataDatabaseDataSource
import com.dreamsoftware.artcollectibles.data.database.datasource.metadata.impl.TokenMetadataDatabaseDataSourceImpl
import com.dreamsoftware.artcollectibles.data.database.room.dao.metadata.ITokenMetadataDAO
import com.dreamsoftware.artcollectibles.data.database.room.database.AppDatabase
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

    @Provides
    @Singleton
    fun provideTokenMetadataDatabaseDataSource(
        tokenMetadataDAO: ITokenMetadataDAO
    ): ITokenMetadataDatabaseDataSource = TokenMetadataDatabaseDataSourceImpl(tokenMetadataDAO)
}
