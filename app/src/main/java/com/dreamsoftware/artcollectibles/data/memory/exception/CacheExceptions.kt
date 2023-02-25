package com.dreamsoftware.artcollectibles.data.memory.exception

/**
 * Cache Exception
 * @param message
 * @param cause
 */
abstract class CacheException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

/**
 * Cache Common Error Exception
 * @param message
 * @param cause
 */
class CacheErrorException(message: String? = null, cause: Throwable? = null): CacheException(message, cause)

/**
 * Cache No Result Exception
 * @param message
 * @param cause
 */
class CacheNoResultException(message: String? = null, cause: Throwable? = null): CacheException(message, cause)