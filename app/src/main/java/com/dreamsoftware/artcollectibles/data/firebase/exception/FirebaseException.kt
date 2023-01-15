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