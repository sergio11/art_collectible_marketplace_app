package com.dreamsoftware.artcollectibles.data.firebase.datasource

import com.dreamsoftware.artcollectibles.data.firebase.exception.SaveSecretException
import com.dreamsoftware.artcollectibles.data.firebase.exception.SecretNotFoundException
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletSecretDTO

interface IWalletSecretsDataSource {

    /**
     * @param secret
     */
    @Throws(SaveSecretException::class)
    suspend fun save(secret: WalletSecretDTO)

    /**
     * @param uid
     */
    @Throws(SecretNotFoundException::class)
    suspend fun getByUserUid(uid: String): WalletSecretDTO
}