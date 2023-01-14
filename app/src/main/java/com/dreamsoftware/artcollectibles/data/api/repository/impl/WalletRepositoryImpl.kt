package com.dreamsoftware.artcollectibles.data.api.repository.impl

import android.util.Log
import com.dreamsoftware.artcollectibles.data.api.exception.WalletDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.AccountBalanceMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IAccountBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IFaucetBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletMetadataDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletMetadataDTO
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import com.dreamsoftware.artcollectibles.utils.PasswordUtils
import org.web3j.crypto.Credentials
import java.net.URL

/**
 * Wallet Repository Implementation
 * @param accountBalanceMapper
 * @param accountBlockchainDataSource
 * @param userCredentialsMapper
 * @param preferencesDataSource
 * @param walletSecretsDataSource
 * @param storageDataSource
 * @param passwordUtils
 * @param walletDataSource
 * @param faucetDataSource
 */
internal class WalletRepositoryImpl(
    private val accountBalanceMapper: AccountBalanceMapper,
    private val accountBlockchainDataSource: IAccountBlockchainDataSource,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val preferencesDataSource: IPreferencesDataSource,
    private val walletSecretsDataSource: IWalletMetadataDataSource,
    private val storageDataSource: IStorageDataSource,
    private val passwordUtils: PasswordUtils,
    private val walletDataSource: IWalletDataSource,
    private val faucetDataSource: IFaucetBlockchainDataSource
): IWalletRepository {

    private companion object {
        const val WALLET_SECRET_LENGTH = 30
        const val WALLET_DIRECTORY = "wallets"
    }

    /**
     * Load credentials
     */
    @Throws(WalletDataException::class)
    override suspend fun loadCredentials(): UserWalletCredentials = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        val credentials = loadCredentials(userUid)
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: Exception) {
        throw WalletDataException("An error occurred when trying to load wallet credentials", ex)
    }

    /**
     * Generate a new wallet
     */
    @Throws(WalletDataException::class)
    override suspend fun generate(userUid: String): UserWalletCredentials = try {
        // Generate Wallet Secret
        val walletSecret = passwordUtils.generatePassword(length = WALLET_SECRET_LENGTH)
        val walletCreated = walletDataSource.generate(walletSecret)
        val walletUri = storageDataSource.save(WALLET_DIRECTORY, walletCreated.name, walletCreated.path.toString())
        walletSecretsDataSource.save(WalletMetadataDTO(userUid, walletCreated.name, walletSecret, walletUri.toString()))
        val credentials = walletDataSource.loadCredentials(walletCreated.name, walletSecret)
        tryToRequestSeedFunds(credentials)
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw WalletDataException("An error occurred when generating a new wallet", ex)
    }

    /**
     * Get current balance for current authenticated user
     */
    @Throws(WalletDataException::class)
    override suspend fun getCurrentBalance(): AccountBalance = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        val credentials = loadCredentials(userUid)
        val balance = accountBlockchainDataSource.getCurrentBalance(credentials)
        accountBalanceMapper.mapInToOut(balance)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw WalletDataException("An error occurred when trying to get current balance", ex)
    }

    /**
     * Private Methods
     */

    private suspend fun tryToRequestSeedFunds(credentials: Credentials) {
        Log.d("FAUCET", "tryToRequestSeedFunds CALLED!")
        try {
            faucetDataSource.requestSeedFunds(credentials)
        } catch (ex: Exception) {
            Log.d("FAUCET", "ex message ${ex.message} CALLED!")
            ex.printStackTrace()
        }
    }

    private suspend fun loadCredentials(userUid: String): Credentials = with(walletDataSource) {
        with(walletSecretsDataSource.getByUserUid(userUid)) {
            if(!hasWallet(name)) {
                walletDataSource.install(name, URL(walletUri))
            }
            loadCredentials(name, password)
        }
    }
}