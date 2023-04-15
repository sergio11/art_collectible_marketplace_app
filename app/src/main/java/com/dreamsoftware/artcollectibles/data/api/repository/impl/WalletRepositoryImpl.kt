package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.api.exception.GetCurrentBalanceException
import com.dreamsoftware.artcollectibles.data.api.exception.LoadWalletCredentialsException
import com.dreamsoftware.artcollectibles.data.api.mapper.AccountBalanceMapper
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IAccountBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IFaucetBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IMarketPricesBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStorageDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletMetadataDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletMetadataDTO
import com.dreamsoftware.artcollectibles.data.memory.datasource.IWalletCredentialsMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.datasource.IWalletMetadataMemoryDataSource
import com.dreamsoftware.artcollectibles.data.memory.exception.CacheException
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
 * @param walletMetadataMemoryCache
 * @param walletCredentialsMemoryDataSource
 * @param marketPricesBlockchainDataSource
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
    private val faucetDataSource: IFaucetBlockchainDataSource,
    private val walletMetadataMemoryCache: IWalletMetadataMemoryDataSource,
    private val walletCredentialsMemoryDataSource: IWalletCredentialsMemoryDataSource,
    private val marketPricesBlockchainDataSource: IMarketPricesBlockchainDataSource
): IWalletRepository {

    private companion object {
        const val WALLET_SECRET_LENGTH = 30
        const val WALLET_DIRECTORY = "wallets"
    }

    /**
     * Load credentials
     */
    @Throws(LoadWalletCredentialsException::class)
    override suspend fun loadCredentials(): UserWalletCredentials = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        val credentials = loadCredentials(userUid)
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: Exception) {
        throw LoadWalletCredentialsException("An error occurred when trying to load wallet credentials", ex)
    }

    /**
     * Generate a new wallet
     */
    @Throws(GenerateWalletException::class)
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
        throw GenerateWalletException("An error occurred when generating a new wallet", ex)
    }

    /**
     * Get current balance for current authenticated user
     */
    @Throws(GetCurrentBalanceException::class)
    override suspend fun getCurrentBalance(): AccountBalance = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        val credentials = loadCredentials(userUid)
        val balance = accountBlockchainDataSource.getCurrentBalance(credentials)
        val marketPrices = marketPricesBlockchainDataSource.getPricesOf(priceInEth = balance.balanceInEth)
        accountBalanceMapper.mapInToOut(AccountBalanceMapper.InputData(balance, marketPrices))
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw GetCurrentBalanceException("An error occurred when trying to get current balance", ex)
    }

    @Throws(GetCurrentBalanceException::class)
    override suspend fun getBalanceOf(targetAddress: String): AccountBalance = try {
        val balance = accountBlockchainDataSource.getBalanceOf(targetAddress)
        val marketPrices = marketPricesBlockchainDataSource.getPricesOf(priceInEth = balance.balanceInEth)
        accountBalanceMapper.mapInToOut(AccountBalanceMapper.InputData(balance, marketPrices))
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw GetCurrentBalanceException("An error occurred when trying to get current balance", ex)
    }

    /**
     * Private Methods
     */

    private suspend fun tryToRequestSeedFunds(credentials: Credentials) {
        try {
            faucetDataSource.requestSeedFunds(credentials)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private suspend fun loadCredentials(userUid: String): Credentials = with(walletMetadataMemoryCache) {
        val walletMetadata = try {
            findByKey(userUid)
        } catch (ex: CacheException) {
            walletSecretsDataSource.getByUserUid(userUid).also {
                save(userUid, it)
            }
        }
        with(walletDataSource) {
            with(walletCredentialsMemoryDataSource) {
                with(walletMetadata) {
                    try {
                        findByKey(name)
                    } catch (ex: CacheException) {
                        if (!hasWallet(name)) {
                            install(name, URL(walletUri))
                        }
                        loadCredentials(name, password).also {
                            save(name, it)
                        }
                    }
                }
            }
        }
    }
}