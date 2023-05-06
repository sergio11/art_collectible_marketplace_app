package com.dreamsoftware.artcollectibles.data.blockchain.exception

open class BlockchainDataSourceException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Art Collectible DataSource
class MintTokenException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class BurnTokenException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class GetTokensCreatedException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class GetTokensOwnedException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class GetTokenByIdException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class GetTokenByCidException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchTokensStatisticsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)

// Art Marketplace Blockchain
class FetchAvailableMarketItemsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchSellingMarketItemsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchOwnedMarketItemsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchMarketHistoryException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class PutItemForSaleException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class WithdrawFromSaleException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class BuyItemException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchItemForSaleException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchMarketHistoryItemException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchItemForSaleByMetadataCIDException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class CheckTokenAddedForSaleException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchMarketplaceStatisticsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchWalletStatisticsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchTokenMarketHistoryException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchCurrentItemPriceException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class FetchTokenMarketHistoryPricesException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
// Wallet DataSource
class LoadWalletCredentialsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class GenerateWalletException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class InstallWalletException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)

// Faucet DataSource
class NotEnoughFundsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class RequestSeedFundsException(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
class ItemNotAvailableForSale(message: String? = null, cause: Throwable? = null): BlockchainDataSourceException(message, cause)
