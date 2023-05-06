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
class FollowUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class UnFollowUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class CheckFollowersUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class GetFollowersUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)
class GetFollowingUserException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)

class GetMostFollowedUsersException(message: String? = null, cause: Throwable? = null): UserDataException(message, cause)

// Wallet Repository Exception
abstract class WalletDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class LoadWalletCredentialsException(message: String? = null, cause: Throwable? = null): WalletDataException(message, cause)
class GenerateWalletException(message: String? = null, cause: Throwable? = null): WalletDataException(message, cause)
class GetCurrentBalanceException(message: String? = null, cause: Throwable? = null): WalletDataException(message, cause)
// ArtCollectible Repository Exception
abstract class ArtCollectibleDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class ObserveArtCollectibleMintedEventsException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class CreateArtCollectibleDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class DeleteArtCollectibleDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokensOwnedDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokensCreatedDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokenByIdDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokensDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)
class GetTokensByCategoryDataException(message: String? = null, cause: Throwable? = null): ArtCollectibleDataException(message, cause)

// ArtMarketplace Repository Exception
abstract class ArtMarketplaceDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class FetchAvailableMarketItemsByCategoryDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchAvailableMarketItemsDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchSellingMarketItemsDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchOwnedMarketItemsDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchMarketHistoryDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchTokenCurrentPriceDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class PutItemForSaleDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchItemForSaleDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchMarketHistoryItemDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class WithdrawFromSaleDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class CheckTokenAddedForSaleDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchMarketplaceStatisticsDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class FetchTokenMarketHistoryPricesDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class BuyItemDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class GetMarketItemsByCategoryDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class GetSimilarMarketItemsDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)
class GetSimilarAuthorMarketItemsDataException(message: String? = null, cause: Throwable? = null): ArtMarketplaceDataException(message, cause)

// Secrets Repository Exception
abstract class SecretDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class GenerateSecretException(message: String? = null, cause: Throwable? = null): SecretDataException(message, cause)
class GetSecretException(message: String? = null, cause: Throwable? = null): SecretDataException(message, cause)
// Favorites Repository Exception
abstract class FavoritesDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class AddToFavoritesDataException(message: String? = null, cause: Throwable? = null): FavoritesDataException(message, cause)
class RemoveFromFavoritesDataException(message: String? = null, cause: Throwable? = null): FavoritesDataException(message, cause)
class GetMostLikedTokensDataException(message: String? = null, cause: Throwable? = null): FavoritesDataException(message, cause)
class GetMyFavoriteTokensDataException(message: String? = null, cause: Throwable? = null): FavoritesDataException(message, cause)
class GetUserLikesByTokenDataException(message: String? = null, cause: Throwable? = null): FavoritesDataException(message, cause)
// Visitors Repository Exception
abstract class VisitorsDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class RegisterVisitorDataException(message: String? = null, cause: Throwable? = null): VisitorsDataException(message, cause)
class GetVisitorsByTokenDataException(message: String? = null, cause: Throwable? = null): VisitorsDataException(message, cause)
class GetMostVisitedTokensDataException(message: String? = null, cause: Throwable? = null): VisitorsDataException(message, cause)
// Token Metadata Repository Exception
abstract class TokenMetadataDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class CreateTokenMetadataDataException(message: String? = null, cause: Throwable? = null): TokenMetadataDataException(message, cause)
class UpdateTokenMetadataDataException(message: String? = null, cause: Throwable? = null): TokenMetadataDataException(message, cause)
class DeleteTokenMetadataDataException(message: String? = null, cause: Throwable? = null): TokenMetadataDataException(message, cause)
class FetchByAuthorAddressDataException(message: String? = null, cause: Throwable? = null): TokenMetadataDataException(message, cause)
class FetchByCidDataException(message: String? = null, cause: Throwable? = null): TokenMetadataDataException(message, cause)
class PreferenceDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)

// Categories Repository Exception
abstract class CategoriesDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class GetCategoriesDataException(message: String? = null, cause: Throwable? = null): CategoriesDataException(message, cause)
class GetCategoryDataException(message: String? = null, cause: Throwable? = null): CategoriesDataException(message, cause)

// Comment Repository Exception
abstract class CommentsDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class GetCommentsByTokenDataException(message: String? = null, cause: Throwable? = null): CommentsDataException(message, cause)
class SaveCommentDataException(message: String? = null, cause: Throwable? = null): CommentsDataException(message, cause)
class DeleteCommentDataException(message: String? = null, cause: Throwable? = null): CommentsDataException(message, cause)
class GetCommentByIdDataException(message: String? = null, cause: Throwable? = null): CommentsDataException(message, cause)
class CountCommentsByTokenDataException(message: String? = null, cause: Throwable? = null): CommentsDataException(message, cause)

// Notification Repository Exception
abstract class NotificationsDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)
class GetNotificationsByUserDataException(message: String? = null, cause: Throwable? = null): NotificationsDataException(message, cause)
class SaveNotificationDataException(message: String? = null, cause: Throwable? = null): NotificationsDataException(message, cause)
class DeleteNotificationDataException(message: String? = null, cause: Throwable? = null): NotificationsDataException(message, cause)
class GetNotificationByIdDataException(message: String? = null, cause: Throwable? = null): NotificationsDataException(message, cause)
class CountNotificationsByUserDataException(message: String? = null, cause: Throwable? = null): NotificationsDataException(message, cause)

// Statistics Repository Exception

abstract class StatisticsDataException(message: String? = null, cause: Throwable? = null): DataRepositoryException(message, cause)

class FetchMarketStatisticsDataException(message: String? = null, cause: Throwable? = null): StatisticsDataException(message, cause)
class RegisterEventDataException(message: String? = null, cause: Throwable? = null): StatisticsDataException(message, cause)