package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.InstallWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import com.dreamsoftware.artcollectibles.data.blockchain.model.WalletDTO
import org.web3j.crypto.Credentials
import java.net.URL

interface IWalletDataSource {

    /**
     * Install wallet
     * @param name
     * @param walletUrl
     */
    @Throws(InstallWalletException::class)
    suspend fun install(name: String, walletUrl: URL)

    /**
     * Has wallet
     * @param name
     */
    @Throws(LoadWalletCredentialsException::class)
    suspend fun hasWallet(name: String): Boolean

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
    suspend fun generate(password: String): WalletDTO

}