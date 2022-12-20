package com.dreamsoftware.artcollectibles.data.blockchain.di

import com.dreamsoftware.artcollectibles.data.blockchain.contracts.ArtCollectibleContract
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
     * Provide Web3J
     *
     * @return
     */
    @Provides
    @Singleton
    fun provideWeb3j(): Web3j {
        return Web3j.build(HttpService("properties.getClientAddress()", OkHttpClient.Builder().build()))
    }
}