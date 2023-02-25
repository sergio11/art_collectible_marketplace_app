package com.dreamsoftware.artcollectibles.data.memory.di

import com.dreamsoftware.artcollectibles.data.memory.datasource.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.impl.MemoryCacheDataSourceImpl
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
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
    fun provideArtCollectibleMemoryCacheDataSource(): IMemoryCacheDataSource<Any, Iterable<ArtCollectible>> =
        MemoryCacheDataSourceImpl()

}