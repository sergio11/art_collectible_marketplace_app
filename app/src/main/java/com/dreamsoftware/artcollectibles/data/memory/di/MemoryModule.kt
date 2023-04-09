package com.dreamsoftware.artcollectibles.data.memory.di

import com.dreamsoftware.artcollectibles.data.memory.datasource.*
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.*
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.ArtCollectibleMemoryCacheDataSourceImpl
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.UserMemoryDataSourceImpl
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.WalletCredentialsMemoryDataSourceImpl
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.WalletMetadataMemoryDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MemoryModule {

    @Provides
    @Singleton
    fun provideArtCollectibleMemoryCacheDataSource(): IArtCollectibleMemoryCacheDataSource = ArtCollectibleMemoryCacheDataSourceImpl()

    @Provides
    @Singleton
    fun provideArtMarketItemMemoryCacheDataSource(): IArtMarketItemMemoryCacheDataSource = ArtMarketItemMemoryCacheDataSourceImpl()

    @Provides
    @Singleton
    fun provideWalletMetadataMemoryDataSource(): IWalletMetadataMemoryDataSource = WalletMetadataMemoryDataSourceImpl()

    @Provides
    @Singleton
    fun provideUserMemoryDataSource(): IUserMemoryDataSource = UserMemoryDataSourceImpl()

    @Provides
    @Singleton
    fun provideWalletCredentialsMemoryDataSource(): IWalletCredentialsMemoryDataSource = WalletCredentialsMemoryDataSourceImpl()
}