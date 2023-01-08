package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.SecretDataException
import com.dreamsoftware.artcollectibles.domain.models.Secret

interface ISecretRepository {

    @Throws(SecretDataException::class)
    suspend fun generate(userUid: String): Secret

    @Throws(SecretDataException::class)
    suspend fun get(userUid: String): Secret
}