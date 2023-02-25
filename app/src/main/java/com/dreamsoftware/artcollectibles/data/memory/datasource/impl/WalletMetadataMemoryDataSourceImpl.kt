package com.dreamsoftware.artcollectibles.data.memory.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.model.WalletMetadataDTO
import com.dreamsoftware.artcollectibles.data.memory.datasource.IWalletMetadataMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl.SupportMemoryCacheDataSourceImpl
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.minutes

internal class WalletMetadataMemoryDataSourceImpl:
    SupportMemoryCacheDataSourceImpl<String, WalletMetadataDTO>(), IWalletMetadataMemoryDataSource{

    companion object {
        private const val MAXIMUM_CACHE_SIZE = 1L
        private val EXPIRE_AFTER_WRITE = 30L.minutes
    }

    override fun buildCache(): Cache<String, WalletMetadataDTO> = Cache.Builder()
        .maximumCacheSize(MAXIMUM_CACHE_SIZE)
        .expireAfterWrite(EXPIRE_AFTER_WRITE)
        .build()
}