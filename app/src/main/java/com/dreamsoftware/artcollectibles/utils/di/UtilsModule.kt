package com.dreamsoftware.artcollectibles.utils.di

import android.content.Context
import com.dreamsoftware.artcollectibles.utils.CryptoUtils
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import com.dreamsoftware.artcollectibles.utils.PasswordUtils
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
}