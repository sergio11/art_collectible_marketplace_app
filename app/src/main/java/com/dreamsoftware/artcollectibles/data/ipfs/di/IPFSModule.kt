package com.dreamsoftware.artcollectibles.data.ipfs.di

import android.content.Context
import com.dreamsoftware.artcollectibles.data.ipfs.config.PinataConfig
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.impl.PinataDataSourceImpl
import com.dreamsoftware.artcollectibles.data.ipfs.serder.DateJsonAdapter
import com.dreamsoftware.artcollectibles.data.ipfs.service.IPinataPinningService
import com.dreamsoftware.artcollectibles.data.ipfs.service.IPinataQueryFilesService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class IPFSModule {

    private companion object {
        const val TIMEOUT_IN_MINUTES: Long = 2
    }

    /**
     * Provide Pinata Config
     */
    @Provides
    @Singleton
    fun providePinataConfig() = PinataConfig()

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
     * @param networkInterceptors
     * @param requestInterceptors
     */
    @Provides
    @Singleton
    fun provideHttpClient(
        context: Context,
        @Named("networkInterceptors") networkInterceptors: Set<@JvmSuppressWildcards Interceptor>,
        @Named("requestInterceptors") requestInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
        .readTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
        .writeTimeout(TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true).apply {
            networkInterceptors.forEach {
                addNetworkInterceptor(it)
            }
            requestInterceptors.forEach {
                addInterceptor(it)
            }
        }.build()

    /**
     * Provide Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        httpClient: OkHttpClient,
        pinataConfig: PinataConfig
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(pinataConfig.pinataBaseUrl)
            .client(httpClient)
            .build()

    /**
     * Provide Pinata Pinning Service
     */
    @Provides
    @Singleton
    fun providePinataPinningService(
        retrofit: Retrofit
    ): IPinataPinningService = retrofit.create(IPinataPinningService::class.java)

    /**
     * Provide Pinata Query Files Service
     */
    @Provides
    @Singleton
    fun providePinataQueryFilesService(
        retrofit: Retrofit
    ): IPinataQueryFilesService = retrofit.create(IPinataQueryFilesService::class.java)

    /**
     * Provide Pinata Data Source
     * @param pinataPinningService
     * @param pinataQueryFilesService
     */
    @Provides
    @Singleton
    fun providePinataDataSource(
        pinataPinningService: IPinataPinningService,
        pinataQueryFilesService: IPinataQueryFilesService
    ): IpfsDataSource =
        PinataDataSourceImpl(pinataPinningService, pinataQueryFilesService)

}