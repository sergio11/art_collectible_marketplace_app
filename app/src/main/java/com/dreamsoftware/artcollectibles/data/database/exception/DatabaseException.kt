package com.dreamsoftware.artcollectibles.data.database.exception

open class DatabaseException(message: String? = null, cause: Throwable? = null): Exception(message, cause)
class DBNoResultException(message: String? = null, cause: Throwable? = null): DatabaseException(message, cause)
class DBErrorException(message: String? = null, cause: Throwable? = null): DatabaseException(message, cause)