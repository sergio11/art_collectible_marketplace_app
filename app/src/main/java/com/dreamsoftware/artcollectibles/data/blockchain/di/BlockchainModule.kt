package com.dreamsoftware.artcollectibles.data.blockchain.di

import android.content.Context
import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.service.IAccountInformationService
import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.crytocompare.service.ICryptoCompareService
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.*
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.*
import com.dreamsoftware.artcollectibles.data.blockchain.di.qualifier.AlchemyOkHttpClient
import com.dreamsoftware.artcollectibles.data.blockchain.di.qualifier.AlchemyRetrofit
import com.dreamsoftware.artcollectibles.data.blockchain.di.qualifier.CryptoCompareOkHttpClient
import com.dreamsoftware.artcollectibles.data.blockchain.di.qualifier.CryptoCompareRetrofit
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.*
import com.dreamsoftware.artcollectibles.data.core.di.NetworkModule
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class BlockchainModule {

    /**
     * Provide Blockchain OkHTTP Client
     */
    @Provides
    @Singleton
    @AlchemyOkHttpClient
    fun provideAlchemyOkHttpClient(
        httpClientBuilder: OkHttpClient.Builder,
    ): OkHttpClient = httpClientBuilder.apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
    }.build()

    /**
     * Provide Retrofit
     */
    @Provides
    @Singleton
    @AlchemyRetrofit
    fun provideAlchemyRetrofit(
        converterFactory: Converter.Factory,
        @AlchemyOkHttpClient httpClient: OkHttpClient,
        blockchainConfig: BlockchainConfig
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(blockchainConfig.alchemyUrl)
            .client(httpClient)
            .build()


    /**
     * Provide Crypto Compare OkHTTP Client
     */
    @Provides
    @Singleton
    @CryptoCompareOkHttpClient
    fun provideCryptoCompareOkHttpClient(
        httpClientBuilder: OkHttpClient.Builder,
    ): OkHttpClient = httpClientBuilder.apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
    }.build()

    /**
     * Provide Crypto Compare Retrofit
     */
    @Provides
    @Singleton
    @CryptoCompareRetrofit
    fun provideCryptoCompareRetrofit(
        converterFactory: Converter.Factory,
        @CryptoCompareOkHttpClient httpClient: OkHttpClient,
        blockchainConfig: BlockchainConfig
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(blockchainConfig.cryptoCompareUrl)
            .client(httpClient)
            .build()

    /**
     * Provide Blockchain Config
     */
    @Provides
    @Singleton
    fun provideBlockchainConfig(): BlockchainConfig = BlockchainConfig()

    /**
     * Provide Web3J
     *
     * @return
     */
    @Provides
    @Singleton
    fun provideWeb3j(
        blockchainConfig: BlockchainConfig,
        @AlchemyOkHttpClient httpClient: OkHttpClient
    ): Web3j = Web3j.build(
        HttpService(
            blockchainConfig.alchemyUrl,
            httpClient
        )
    )


    /**
     * Provide Art Collectible Mapper
     */
    @Provides
    @Singleton
    fun provideArtCollectibleMapper(): ArtCollectibleMapper = ArtCollectibleMapper()

    /**
     * Provide Art Marketplace Mapper
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceMapper(
        artCollectibleForSalePricesMapper: ArtCollectibleForSalePricesMapper
    ): ArtMarketplaceMapper = ArtMarketplaceMapper(artCollectibleForSalePricesMapper)

    /**
     * Provide art Collectible Minted event mapper
     */
    @Provides
    @Singleton
    fun provideArtCollectibleMintedEventMapper(): ArtCollectibleMintedEventMapper = ArtCollectibleMintedEventMapper()

    /**
     * Provide Token Statistics
     */
    @Provides
    @Singleton
    fun provideTokenStatisticsMapper(): TokenStatisticsMapper = TokenStatisticsMapper()


    @Provides
    @Singleton
    fun provideMarketStatisticsMapper(): MarketStatisticsMapper = MarketStatisticsMapper()


    @Provides
    @Singleton
    fun provideArtCollectibleMarketPriceMapper(
        artCollectibleForSalePricesMapper: ArtCollectibleForSalePricesMapper
    ): ArtCollectibleMarketPriceMapper = ArtCollectibleMarketPriceMapper(artCollectibleForSalePricesMapper)

    @Provides
    @Singleton
    fun provideArtCollectibleForSalePricesMapper(): ArtCollectibleForSalePricesMapper = ArtCollectibleForSalePricesMapper()

    /**
     * Provider Wallet Data Source
     */
    @Provides
    @Singleton
    fun providerWalletDataSource(
        @ApplicationContext appContext: Context,
        applicationAware: IApplicationAware
    ): IWalletDataSource =
        WalletDataSourceImpl(appContext, applicationAware)

    /**
     * Provide Art Collectible Data Source
     */
    @Provides
    @Singleton
    fun provideArtCollectibleDataSource(
        artCollectibleMapper: ArtCollectibleMapper,
        artCollectibleMintedEventMapper: ArtCollectibleMintedEventMapper,
        tokenStatisticsMapper: TokenStatisticsMapper,
        blockchainConfig: BlockchainConfig,
        web3j: Web3j
    ): IArtCollectibleBlockchainDataSource =
        ArtCollectibleBlockchainDataSourceImpl(
            artCollectibleMapper,
            artCollectibleMintedEventMapper,
            tokenStatisticsMapper,
            blockchainConfig,
            web3j
        )

    /**
     * Provide Art Marketplace Data Source
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceDataSource(
        artMarketplaceMapper: ArtMarketplaceMapper,
        marketStatisticsMapper: MarketStatisticsMapper,
        walletStatisticsMapper: WalletStatisticsMapper,
        artCollectibleMarketPriceMapper: ArtCollectibleMarketPriceMapper,
        artCollectibleForSalePricesMapper: ArtCollectibleForSalePricesMapper,
        blockchainConfig: BlockchainConfig,
        web3j: Web3j
    ): IArtMarketplaceBlockchainDataSource =
        ArtMarketplaceBlockchainDataSourceImpl(
            artMarketplaceMapper,
            marketStatisticsMapper,
            walletStatisticsMapper,
            artCollectibleMarketPriceMapper,
            artCollectibleForSalePricesMapper,
            blockchainConfig,
            web3j
        )

    /**
     * Provide Faucet Blockchain Data Source
     * @param blockchainConfig
     * @param web3j
     */
    @Provides
    @Singleton
    fun provideFaucetBlockchainDataSource(
        blockchainConfig: BlockchainConfig,
        web3j: Web3j
    ): IFaucetBlockchainDataSource =
        FaucetBlockchainDataSourceImpl(
            blockchainConfig,
            web3j
        )

    /**
     * Provide Account Blockchain Data Source
     * @param accountInformationService
     * @param web3j
     */
    @Provides
    @Singleton
    fun provideAccountBlockchainDataSource(
        accountInformationService: IAccountInformationService,
        web3j: Web3j
    ): IAccountBlockchainDataSource =
        AccountBlockchainDataSourceImpl(
            accountInformationService,
            web3j
        )

    /**
     * Provide market prices blockchain data source
     * @param cryptoCompareService
     * @param web3j
     */
    @Provides
    @Singleton
    fun provideMarketPricesBlockchainDataSource(
        cryptoCompareService: ICryptoCompareService,
        web3j: Web3j
    ): IMarketPricesBlockchainDataSource =
        MarketPricesBlockchainDataSourceImpl(cryptoCompareService, web3j)

    /**
     * Provide Account information service
     * @param retrofit
     */
    @Provides
    @Singleton
    fun provideAccountInformationService(
        @AlchemyRetrofit retrofit: Retrofit
    ): IAccountInformationService = retrofit.create(IAccountInformationService::class.java)


    /**
     * Provide Crypto compare service
     * @param retrofit
     */
    @Provides
    @Singleton
    fun provideCryptoCompareService(
        @CryptoCompareRetrofit retrofit: Retrofit
    ): ICryptoCompareService = retrofit.create(ICryptoCompareService::class.java)
}