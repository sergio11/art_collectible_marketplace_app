package com.dreamsoftware.artcollectibles.data.ipfs.di

import com.dreamsoftware.artcollectibles.data.core.di.NetworkModule
import com.dreamsoftware.artcollectibles.data.ipfs.config.PinataConfig
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.impl.PinataDataSourceImpl
import com.dreamsoftware.artcollectibles.data.ipfs.di.qualifier.PinataOkHttpClient
import com.dreamsoftware.artcollectibles.data.ipfs.di.qualifier.PinataRetrofit
import com.dreamsoftware.artcollectibles.data.ipfs.mapper.CreateTokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.ipfs.mapper.TokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.ipfs.mapper.UpdateTokenMetadataMapper
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.interceptor.PinataAuthInterceptor
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.service.IPinataPinningService
import com.dreamsoftware.artcollectibles.data.ipfs.pinata.service.IPinataQueryFilesService
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class IPFSModule {

    /**
     * Provide Pinata Config
     */
    @Provides
    @Singleton
    fun providePinataConfig() = PinataConfig()

    /**
     * Provide Create Token Metadata Mapper
     */
    @Provides
    @Singleton
    fun provideCreateTokenMetadataMapper() = CreateTokenMetadataMapper()

    /**
     * Provide Update Token Metadata Mapper
     */
    @Provides
    @Singleton
    fun provideUpdateTokenMetadataMapper() = UpdateTokenMetadataMapper()

    /**
     * Provide Token Metadata Mapper
     * @param pinataConfig
     */
    @Provides
    @Singleton
    fun provideTokenMetadataMapper(pinataConfig: PinataConfig) = TokenMetadataMapper(pinataConfig)

    /**
     * Provide Auth Interceptor
     * @param pinataConfig
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        pinataConfig: PinataConfig
    ): PinataAuthInterceptor = PinataAuthInterceptor(pinataConfig)

    /**
     * Provide Ok HTTP Client
     * @param pinataAuthInterceptor
     * @param httpClientBuilder
     */
    @Provides
    @Singleton
    @PinataOkHttpClient
    fun providePinataOkHttpClient(
        pinataAuthInterceptor: PinataAuthInterceptor,
        httpClientBuilder: OkHttpClient.Builder,
    ): OkHttpClient = httpClientBuilder.apply {
        addInterceptor(pinataAuthInterceptor)
        addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        })
    }.build()

    /**
     * Provide Retrofit
     * @param converterFactory
     * @param httpClient,
     * @param pinataConfig
     */
    @Provides
    @Singleton
    @PinataRetrofit
    fun provideIpfsRetrofit(
        converterFactory: Converter.Factory,
        @PinataOkHttpClient httpClient: OkHttpClient,
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
        @PinataRetrofit retrofit: Retrofit
    ): IPinataPinningService = retrofit.create(IPinataPinningService::class.java)

    /**
     * Provide Pinata Query Files Service
     */
    @Provides
    @Singleton
    fun providePinataQueryFilesService(
        @PinataRetrofit retrofit: Retrofit
    ): IPinataQueryFilesService = retrofit.create(IPinataQueryFilesService::class.java)

    /**
     * Provide Pinata Data Source
     * @param applicationAware
     * @param pinataPinningService
     * @param pinataQueryFilesService
     * @param createTokenMetadataMapper
     * @param updateTokenMetadataMapper
     * @param tokenMetadataMapper
     */
    @Provides
    @Singleton
    fun providePinataDataSource(
        applicationAware: IApplicationAware,
        pinataPinningService: IPinataPinningService,
        pinataQueryFilesService: IPinataQueryFilesService,
        createTokenMetadataMapper: CreateTokenMetadataMapper,
        updateTokenMetadataMapper: UpdateTokenMetadataMapper,
        tokenMetadataMapper: TokenMetadataMapper
    ): IpfsDataSource =
        PinataDataSourceImpl(
            applicationAware,
            pinataPinningService,
            pinataQueryFilesService,
            createTokenMetadataMapper,
            updateTokenMetadataMapper,
            tokenMetadataMapper
        )
}