package com.dreamsoftware.artcollectibles.data.api.exception

open class DataRepositoryException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

class UserDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class WalletDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class ArtCollectibleDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class SecretDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class PreferenceDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)