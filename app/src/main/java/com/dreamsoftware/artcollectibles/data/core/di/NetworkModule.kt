package com.dreamsoftware.artcollectibles.data.core.di

import com.dreamsoftware.artcollectibles.data.core.network.serder.DateJsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private companion object {
        const val TIMEOUT_IN_MINUTES: Long = 2
    }

    /**
     * Provide Converter Factory
     */
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory =
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(DateJsonAdapter())
                .build()
        )

    /**
     * Provide HTTP Client
     */
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
        .readTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
        .writeTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()
}