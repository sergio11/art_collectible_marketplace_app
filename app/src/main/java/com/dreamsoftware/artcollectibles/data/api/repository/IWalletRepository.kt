package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.WalletDataException
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials

interface IWalletRepository {

    /**
     * Load Credentials
     */
    @Throws(WalletDataException::class)
    suspend fun loadCredentials(): UserWalletCredentials

    /**
     * Generate Credentials
     */
    @Throws(WalletDataException::class)
    suspend fun generate(): UserWalletCredentials
}