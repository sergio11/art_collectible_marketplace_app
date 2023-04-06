package com.dreamsoftware.artcollectibles.data.memory.datasource.impl

import com.dreamsoftware.artcollectibles.data.memory.datasource.IArtCollectibleMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl.SupportMemoryCacheDataSourceImpl
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.minutes

internal class ArtCollectibleMemoryCacheDataSourceImpl :
    SupportMemoryCacheDataSourceImpl<Any, Iterable<ArtCollectible>>(),
    IArtCollectibleMemoryCacheDataSource {

    companion object {
        private const val MAXIMUM_CACHE_SIZE = 20L
        private val EXPIRE_AFTER_WRITE = 4L.minutes
    }

    override fun buildCache(): Cache<Any, Iterable<ArtCollectible>> = Cache.Builder()
        .maximumCacheSize(MAXIMUM_CACHE_SIZE)
        .expireAfterWrite(EXPIRE_AFTER_WRITE)
        .build()
}