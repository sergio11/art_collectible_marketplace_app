package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.SecretDataException

interface ISecretRepository {

    @Throws(SecretDataException::class)
    suspend fun generate(userUid: String): String

    @Throws(SecretDataException::class)
    suspend fun get(userUid: String): String
}