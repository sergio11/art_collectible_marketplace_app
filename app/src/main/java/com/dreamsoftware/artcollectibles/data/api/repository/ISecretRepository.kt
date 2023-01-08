package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.SecretDataException
import com.dreamsoftware.artcollectibles.domain.models.PBEData

interface ISecretRepository {

    @Throws(SecretDataException::class)
    suspend fun generate(userUid: String): PBEData

    @Throws(SecretDataException::class)
    suspend fun get(userUid: String): PBEData
}