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

// Visitors Data Source
class AddVisitorException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetVisitorException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Followers Data Source
class AddFollowerException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class RemoveFollowerException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetFollowersException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetFollowingException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CountFollowersException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CountFollowingException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class CheckFollowerException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)

// Categories Data Source
class GetCategoriesException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class GetCategoryException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
