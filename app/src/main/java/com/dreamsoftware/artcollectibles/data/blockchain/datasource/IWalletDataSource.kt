package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import org.web3j.crypto.Credentials

interface IWalletDataSource {

    /**
     * Load credentials from wallet
     * @param name
     * @param password
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun loadCredentials(name: String, password: String): Credentials

    /**
     * Generate wallet
     * @param password
     */
    @Throws(GenerateWalletException::class)
    suspend fun generate(password: String): String

}