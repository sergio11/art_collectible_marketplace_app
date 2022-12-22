package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import org.web3j.crypto.Credentials

interface IWalletDataSource {

    /**
     * Load credentials from current wallet
     * @param password
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun loadCredentials(): Credentials

    /**
     * Load credentials from wallet
     * @param password
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun loadCredentials(password: String): Credentials

    /**
     * Generate wallet
     * @param password
     */
    @Throws(GenerateWalletException::class)
    suspend fun generate(password: String): String

}