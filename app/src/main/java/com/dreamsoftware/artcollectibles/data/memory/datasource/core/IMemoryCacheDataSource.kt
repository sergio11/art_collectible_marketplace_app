package com.dreamsoftware.artcollectibles.data.memory.datasource.core

import com.dreamsoftware.artcollectibles.data.memory.exception.CacheErrorException
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheNoResultException

interface IMemoryCacheDataSource<in K, T> {

    /**
     * Find All
     */
    @Throws(CacheNoResultException::class, CacheErrorException::class)
    fun findAll(): Iterable<T>

    /**
     * Find By Id
     * @param key
     */
    @Throws(CacheNoResultException::class, CacheErrorException::class)
    fun findByKey(key: K): T

    /**
     * Find by keys
     * @param keys
     * @param strictMode
     */
    @Throws(CacheNoResultException::class, CacheErrorException::class)
    fun findByKeys(keys: Iterable<K>, strictMode: Boolean = true): Iterable<T>

    /**
     * Save Data
     * @param key
     * @param data
     */
    @Throws(CacheErrorException::class)
    fun save(key: K, data: T)

    /**
     * Delete All Data
     */
    @Throws(CacheErrorException::class)
    fun delete()

    /**
     * Delete single data
     */
    @Throws(CacheErrorException::class)
    fun delete(key: K)
}