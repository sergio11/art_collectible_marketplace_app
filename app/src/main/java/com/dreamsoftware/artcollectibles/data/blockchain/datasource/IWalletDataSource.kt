package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import kotlinx.coroutines.flow.Flow
import org.web3j.crypto.Credentials

interface IWalletDataSource {

    /**
     * Load credentials from current wallet
     * @param password
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun loadCredentials(): Flow<Credentials>

    /**
     * Load credentials from wallet
     * @param password
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun loadCredentials(password: String): Flow<Credentials>

    /**
     * Generate wallet
     * @param password
     */
    @Throws(GenerateWalletException::class)
    suspend fun generate(password: String): Flow<String>

}