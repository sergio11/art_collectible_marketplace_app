package com.dreamsoftware.artcollectibles.data.api.exception

open class DataRepositoryException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// User Repository Exception
abstract class UserDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class CheckAuthenticatedException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SignInException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SignUpException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class GetDetailException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SaveUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class UpdateProfilePictureException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class CloseSessionException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class SearchUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)

// Wallet Repository Exception
abstract class WalletDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class LoadWalletCredentialsException(message: String? = null, cause: Throwable? = null): WalletDataException(message, cause)
class GenerateWalletException(message: String? = null, cause: Throwable? = null): WalletDataException(message, cause)
class GetCurrentBalanceException(message: String? = null, cause: Throwable? = null): WalletDataException(message, cause)
// ArtCollectible Repository Exception
abstract class ArtCollectibleDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class ObserveArtCollectibleMintedEventsException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class CreateArtCollectibleException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class DeleteArtCollectibleException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokensOwnedException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokensCreatedException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokenByIdException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
// ArtMarketplace Repository Exception
abstract class ArtMarketplaceDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class FetchAvailableMarketItemsException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchSellingMarketItemsException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchOwnedMarketItemsException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchMarketHistoryException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class PutItemForSaleException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchItemForSaleException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class WithdrawFromSaleException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class CheckTokenAddedForSaleException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchMarketplaceStatisticsException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class BuyItemException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
// Secrets Repository Exception
abstract class SecretDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class GenerateSecretException(message: String? = null, cause: Throwable? = null): SecretDataException(message, cause)
class GetSecretException(message: String? = null, cause: Throwable? = null): SecretDataException(message, cause)

class PreferenceDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)