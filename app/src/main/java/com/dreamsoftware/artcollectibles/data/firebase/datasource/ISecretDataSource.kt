package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveSecretException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SecretNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO

interface ISecretDataSource {

    /**
     * @param secret
     */
    @Throws(SaveSecretException::class)
    suspend fun save(secret: SecretDTO)

    /**
     * @param uid
     */
    @Throws(SecretNotFoundException::class)
    suspend fun getByUserUid(uid: String): SecretDTO
}