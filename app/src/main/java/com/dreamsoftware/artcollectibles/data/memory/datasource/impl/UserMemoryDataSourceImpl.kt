package com.dreamsoftware.artcollectibles.data.memory.datasource.impl

import com.dreamsoftware.artcollectibles.data.memory.datasource.IUserMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl.SupportMemoryCacheDataSourceImpl
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.minutes

internal class UserMemoryDataSourceImpl:
    SupportMemoryCacheDataSourceImpl<String, UserInfo>(), IUserMemoryDataSource{

    companion object {
        private const val MAXIMUM_CACHE_SIZE = 1L
        private val EXPIRE_AFTER_WRITE = 3L.minutes
    }

    override fun buildCache(): Cache<String, UserInfo> = Cache.Builder()
        .maximumCacheSize(MAXIMUM_CACHE_SIZE)
        .expireAfterWrite(EXPIRE_AFTER_WRITE)
        .build()
}