package com.dreamsoftware.artcollectibles.data.memory.datasource.impl

import com.dreamsoftware.artcollectibles.data.memory.datasource.IWalletCredentialsMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl.SupportMemoryCacheDataSourceImpl
import io.github.reactivecircus.cache4k.Cache
import org.web3j.crypto.Credentials
import kotlin.time.Duration.Companion.minutes

internal class WalletCredentialsMemoryDataSourceImpl:
    SupportMemoryCacheDataSourceImpl<String, Credentials>(), IWalletCredentialsMemoryDataSource{

    companion object {
        private const val MAXIMUM_CACHE_SIZE = 1L
        private val EXPIRE_AFTER_WRITE = 20L.minutes
    }

    override fun buildCache(): Cache<String, Credentials> = Cache.Builder()
        .maximumCacheSize(MAXIMUM_CACHE_SIZE)
        .expireAfterWrite(EXPIRE_AFTER_WRITE)
        .build()
}