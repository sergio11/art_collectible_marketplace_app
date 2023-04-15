package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.api.exception.GetCurrentBalanceException
import com.dreamsoftware.artcollectibles.data.api.exception.LoadWalletCredentialsException
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials

interface IWalletRepository {

    /**
     * Load Credentials
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun loadCredentials(): UserWalletCredentials

    /**
     * Generate Credentials
     * @param userUid
     */
    @Throws(GenerateWalletException::class)
    suspend fun generate(userUid: String): UserWalletCredentials

    /**
     * Get Current Balance
     */
    @Throws(GetCurrentBalanceException::class)
    suspend fun getCurrentBalance(): AccountBalance

    /**
     * Get balance of
     * @param targetAddress
     */
    @Throws(GetCurrentBalanceException::class)
    suspend fun getBalanceOf(targetAddress: String): AccountBalance
}