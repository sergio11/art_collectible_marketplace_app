package com.dreamsoftware.artcollectibles.data.di

import com.dreamsoftware.artcollectibles.data.api.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.data.api.impl.ArtCollectibleRepositoryImpl
import com.dreamsoftware.artcollectibles.data.api.impl.ArtMarketplaceRepositoryImpl
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtCollectibleBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IArtMarketplaceBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.di.BlockchainModule
import com.dreamsoftware.artcollectibles.data.ipfs.config.PinataConfig
import com.dreamsoftware.artcollectibles.data.ipfs.datasource.IpfsDataSource
import com.dreamsoftware.artcollectibles.data.ipfs.di.IPFSModule
import com.dreamsoftware.artcollectibles.data.preferences.di.PreferencesModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [BlockchainModule::class, IPFSModule::class, PreferencesModule::class])
@InstallIn(SingletonComponent::class)
class DataModule {

    /**
     * Provide Art Collectibles Repository
     * @param artCollectibleDataSource
     * @param ipfsDataSource
     */
    @Provides
    @Singleton
    fun provideArtCollectiblesRepository(
        artCollectibleDataSource: IArtCollectibleBlockchainDataSource,
        pinataConfig: PinataConfig,
        ipfsDataSource: IpfsDataSource,
        walletDataSource: IWalletDataSource
    ): IArtCollectibleRepository =
        ArtCollectibleRepositoryImpl(
            artCollectibleDataSource,
            pinataConfig,
            ipfsDataSource,
            walletDataSource
        )


    /**
     * Provide Art Marketplace Repository
     * @param artMarketplaceBlockchainDataSource
     * @param ipfsDataSource
     */
    @Provides
    @Singleton
    fun provideArtMarketplaceRepository(
        artMarketplaceBlockchainDataSource: IArtMarketplaceBlockchainDataSource,
        ipfsDataSource: IpfsDataSource
    ): IArtMarketplaceRepository =
        ArtMarketplaceRepositoryImpl(artMarketplaceBlockchainDataSource, ipfsDataSource)

}
