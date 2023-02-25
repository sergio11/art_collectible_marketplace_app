package com.dreamsoftware.artcollectibles.data.memory.di

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract.ArtCollectible
import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IWalletMetadataMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl.SupportMemoryCacheDataSourceImpl
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.ArtCollectibleMemoryCacheDataSourceImpl
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
    fun provideWalletMetadataMemoryDataSource(): IWalletMetadataMemoryDataSource = WalletMetadataMemoryDataSourceImpl()
}