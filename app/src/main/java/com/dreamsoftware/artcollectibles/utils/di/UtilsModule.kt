package com.dreamsoftware.artcollectibles.utils.di

import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}