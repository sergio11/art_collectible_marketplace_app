package com.dreamsoftware.artcollectibles.data.firebase.exception

open class FirebaseException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

class AuthException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class UserNotFoundException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class SaveUserException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)
class UserErrorException(message: String? = null, cause: Throwable? = null): FirebaseException(message, cause)