package com.dreamsoftware.artcollectibles.data.memory.datasource.core.impl

import com.dreamsoftware.artcollectibles.data.memory.datasource.core.IMemoryCacheDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheErrorException
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheNoResultException
import io.github.reactivecircus.cache4k.Cache

abstract class SupportMemoryCacheDataSourceImpl<in K: Any, T: Any>: IMemoryCacheDataSource< K, T> {

    private val cache by lazy {
        buildCache()
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

    override fun hasKey(key: K): Boolean = runCatching {
        cache.get(key) != null
    }.getOrDefault(false)

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

    abstract fun buildCache(): Cache<K, T>
}