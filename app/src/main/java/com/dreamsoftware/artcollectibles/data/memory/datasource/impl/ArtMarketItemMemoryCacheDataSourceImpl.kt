package com.dreamsoftware.artcollectibles.data.memory.datasource.impl

import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtMarketItemMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl.SupportMemoryCacheDataSourceImpl
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.minutes

internal class ArtMarketItemMemoryCacheDataSourceImpl :
    SupportMemoryCacheDataSourceImpl<Any, ArtCollectibleForSale>(),
    IArtMarketItemMemoryCacheDataSource {

    companion object {
        private const val MAXIMUM_CACHE_SIZE = 30L
        private val EXPIRE_AFTER_WRITE = 2L.minutes
    }

    override fun buildCache(): Cache<Any, ArtCollectibleForSale> = Cache.Builder()
        .maximumCacheSize(MAXIMUM_CACHE_SIZE)
        .expireAfterWrite(EXPIRE_AFTER_WRITE)
        .build()
}