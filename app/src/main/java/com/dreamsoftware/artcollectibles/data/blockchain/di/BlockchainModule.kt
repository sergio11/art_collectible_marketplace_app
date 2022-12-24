package com.dreamsoftware.artcollectibles.data.blockchain.di

import android.content.Context
import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.ArtCollectibleBlockchainDataSourceImpl
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.ArtMarketplaceBlockchainDataSourceImpl
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl.WalletDataSourceImpl
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtCollectibleMapper
import com.dreamsoftware.artcollectibles.data.blockchain.mapper.ArtMarketplaceMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideWeb3j(blockchainConfig: BlockchainConfig): Web3j = Web3j.build(
        HttpService(
            blockchainConfig.alchemyUrl,
            OkHttpClient.Builder().build()
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
    fun provideArtMarketplaceMapper(): ArtMarketplaceMapper = ArtMarketplaceMapper()

    /**
     * Provider Wallet Data Source
     */
    @Provides
    @Singleton
    fun providerWalletDataSource(
        @ApplicationContext appContext: Context
    ): IWalletDataSource =
        WalletDataSourceImpl(appContext)

    /**
     * Provide Art Collectible Data Source
     */
    @Provides
    @Singleton
    fun provideArtCollectibleDataSource(
        artCollectibleMapper: ArtCollectibleMapper,
        blockchainConfig: BlockchainConfig,
        web3j: Web3j
    ): IArtCollectibleBlockchainDataSource =
        ArtCollectibleBlockchainDataSourceImpl(
            artCollectibleMapper,
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
        blockchainConfig: BlockchainConfig,
        walletDataSource: IWalletDataSource,
        web3j: Web3j
    ): IArtMarketplaceBlockchainDataSource =
        ArtMarketplaceBlockchainDataSourceImpl(
            artMarketplaceMapper,
            blockchainConfig,
            web3j
        )
}