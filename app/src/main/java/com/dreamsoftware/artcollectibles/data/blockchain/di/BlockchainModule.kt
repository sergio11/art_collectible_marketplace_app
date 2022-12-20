package com.dreamsoftware.artcollectibles.data.blockchain.di

import android.content.Context
import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.ArtCollectibleDataSourceImpl
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.ArtMarketplaceDataSourceImpl
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.WalletDataSourceImpl
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BlockchainModule {

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
    fun provideWeb3j(blockchainConfig: BlockchainConfig): Web3j = Web3j.build(HttpService(
        blockchainConfig.alchemyUrl,
        OkHttpClient.Builder().build()
    ))



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
    fun provideArtMarketplaceMapper(): ArtMarketplaceMapper = ArtMarketplaceMapper()

    /**
     * Provider Wallet Data Source
     */
    @Provides
    @Singleton
    fun providerWalletDataSource(appContext: Context): IWalletDataSource =
        WalletDataSourceImpl(appContext)

    /**
     * Provide Art Collectible Data Source
     */
    @Provides
    @Singleton
    fun provideArtCollectibleDataSource(
        artCollectibleMapper: ArtCollectibleMapper,
        blockchainConfig: BlockchainConfig,
        walletDataSource: IWalletDataSource,
        web3j: Web3j
    ): IArtCollectibleDataSource =
        ArtCollectibleDataSourceImpl(
            artCollectibleMapper,
            blockchainConfig,
            walletDataSource,
            web3j
        )

    /**
     * Provide Art Marketplace Data Source
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceDataSource(
        artMarketplaceMapper: ArtMarketplaceMapper,
        blockchainConfig: BlockchainConfig,
        walletDataSource: IWalletDataSource,
        web3j: Web3j
    ): IArtMarketplaceDataSource =
        ArtMarketplaceDataSourceImpl(
            artMarketplaceMapper,
            blockchainConfig,
            walletDataSource,
            web3j
        )
}