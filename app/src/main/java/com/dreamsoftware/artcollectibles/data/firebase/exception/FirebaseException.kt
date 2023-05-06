package com.dreamsoftware.artcollectibles.data.firebase.exception

open class FirebaseException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Auth Data Source
class AuthException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SignInException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SignUpException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// User DataSource
class UserNotFoundException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SaveUserException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class UserErrorException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Wallet Metadata Data Source
class WalletMetadataNotFoundException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SaveWalletMetadataException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Storage Data Source
class FileNotFoundException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SaveFileException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class RemoveFileException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Secret Data Source
class SecretNotFoundException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SaveSecretException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Favorites Data Source
class GetFavoritesException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class AddToFavoritesException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class RemoveFromFavoritesException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetMostLikedTokensException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetUserLikesByTokenException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
// Visitors Data Source
class AddVisitorException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetVisitorsCountException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetVisitorsByTokenException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetMostVisitedTokensException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
// Followers Data Source
class AddFollowerException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class RemoveFollowerException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetFollowersException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetFollowingException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CountFollowersException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CountFollowingException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CheckFollowerException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

class GetMostFollowedUsersException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Categories Data Source
class GetCategoriesException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetCategoryException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class AddTokenToCategoryException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class RemoveTokenFromCategoryException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetTokensByCategoryException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Comments Data Source
class SaveCommentException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetCommentsByTokenIdException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CountCommentsException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetCommentByIdException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class DeleteCommentException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Notifications Data Source
class SaveNotificationException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetNotificationsByUserException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CountNotificationsException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetNotificationByIdException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class DeleteNotificationException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Statistics Data Source

class FetchMarketStatisticsException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

class RegisterEventException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)