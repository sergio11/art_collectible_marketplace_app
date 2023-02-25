package com.dreamsoftware.artcollectibles.data.memory.datasource.impl

import com.dreamsoftware.artcollectibles.data.memory.datasource.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheErrorException
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheNoResultException
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.minutes

internal class MemoryCacheDataSourceImpl<in K: Any, T: Any>: IMemoryCacheDataSource<K, T> {

    private val cache by lazy {
        Cache.Builder()
            .maximumCacheSize(MAXIMUM_CACHE_SIZE)
            .expireAfterWrite(EXPIRE_AFTER_WRITE)
            .build<K, T>()
    }

    @Throws(CacheNoResultException::class, CacheErrorException::class)
    override fun findAll(): List<T> = try {
        cache.asMap().let { items ->
            if(items.isEmpty())
                throw CacheNoResultException()
            items.map {
                it.value
            }
        }
    } catch (ex: CacheException) {
        throw ex
    } catch (ex: Exception) {
        throw CacheErrorException("Failed to get from cache", ex)
    }

    @Throws(CacheNoResultException::class, CacheErrorException::class)
    override fun findByKeys(keys: Iterable<K>, strictMode: Boolean): Iterable<T> = try {
        if(strictMode) {
            keys.map { cache.get(it) ?: throw CacheNoResultException() }
        } else {
            keys.mapNotNull { cache.get(it) }
        }
    } catch (ex: CacheException) {
        throw ex
    } catch (ex: Exception) {
        throw CacheErrorException("Failed to get from cache", ex)
    }

    @Throws(CacheNoResultException::class, CacheErrorException::class)
    override fun findByKey(key: K): T = try {
        cache.get(key) ?: throw CacheNoResultException()
    } catch (ex: CacheException) {
        throw ex
    } catch (ex: Exception) {
        throw CacheErrorException("Failed to get from cache", ex)
    }

    @Throws(CacheErrorException::class)
    override fun save(key: K, data: T) = try {
        cache.put(key, data)
    } catch (ex: CacheException) {
        throw ex
    } catch (ex: Exception) {
        throw CacheErrorException("Cache Error", ex)
    }

    @Throws(CacheErrorException::class)
    override fun delete() = try {
        cache.invalidateAll()
    } catch (ex: Exception) {
        throw CacheErrorException("Failed to delete from cache", ex)
    }

    @Throws(CacheErrorException::class)
    override fun delete(key: K) = try {
        cache.invalidate(key)
    } catch (ex: Exception) {
        throw CacheErrorException("Failed to delete from cache", ex)
    }

    companion object {
        private const val MAXIMUM_CACHE_SIZE = 10L
        private val EXPIRE_AFTER_WRITE = 5L.minutes
    }
}