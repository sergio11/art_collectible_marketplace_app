package com.dreamsoftware.artcollectibles.data.blockchain.exception

open class BlockchainDataSourceException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Wallet DataSource
class LoadWalletCredentialsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class GenerateWalletException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class InstallWalletException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)

// Faucet DataSource
class NotEnoughFundsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class RequestSeedFundsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class ItemNotAvailableForSale(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
