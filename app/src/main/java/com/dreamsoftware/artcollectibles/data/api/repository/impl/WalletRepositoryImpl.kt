package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.WalletDataException
import com.dreamsoftware.artcollectibles.data.api.mapper.AccountBalanceMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IAccountBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletSecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletSecretDTO
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import com.dreamsoftware.artcollectibles.utils.SecretUtils

/**
 * Wallet Repository Implementation
 * @param accountBalanceMapper
 * @param accountBlockchainDataSource
 * @param userCredentialsMapper
 * @param preferencesDataSource
 * @param walletSecretsDataSource
 * @param storageDataSource
 * @param secretUtils
 * @param walletDataSource
 */
internal class WalletRepositoryImpl(
    private val accountBalanceMapper: AccountBalanceMapper,
    private val accountBlockchainDataSource: IAccountBlockchainDataSource,
    private val userCredentialsMapper: UserCredentialsMapper,
    private val preferencesDataSource: IPreferencesDataSource,
    private val walletSecretsDataSource: IWalletSecretsDataSource,
    private val storageDataSource: IStorageDataSource,
    private val secretUtils: SecretUtils,
    private val walletDataSource: IWalletDataSource,
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
        val walletSecret = walletSecretsDataSource.getByUserUid(userUid)
        val credentials = walletDataSource.loadCredentials(walletSecret.name, walletSecret.secret)
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
        val walletSecret = secretUtils.generatePassword(
            isWithLetters = true,
            isWithSpecial = true,
            isWithNumbers = true,
            isWithUppercase = true,
            length = WALLET_SECRET_LENGTH
        )
        val walletCreated = walletDataSource.generate(walletSecret)
        val walletUri = storageDataSource.save(WALLET_DIRECTORY, walletCreated.name, walletCreated.path.toString())
        walletSecretsDataSource.save(WalletSecretDTO(userUid, walletCreated.name, walletSecret, walletUri.toString()))
        val credentials = walletDataSource.loadCredentials(walletCreated.name, walletSecret)
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
        val walletSecret = walletSecretsDataSource.getByUserUid(userUid)
        val credentials = walletDataSource.loadCredentials(walletSecret.name, walletSecret.secret)
        val balance = accountBlockchainDataSource.getCurrentBalance(credentials)
        accountBalanceMapper.mapInToOut(balance)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw WalletDataException("An error occurred when trying to get current balance", ex)
    }
}