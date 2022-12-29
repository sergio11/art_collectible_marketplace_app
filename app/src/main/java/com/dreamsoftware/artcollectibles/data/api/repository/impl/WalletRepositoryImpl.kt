package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.DataRepositoryException
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.ISecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.SecretDTO
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import com.dreamsoftware.artcollectibles.utils.SecretUtils
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security


/**
 * Wallet Repository Implementation
 * @param userCredentialsMapper
 * @param preferencesDataSource
 * @param secretsDataSource
 * @param walletDataSource
 */
internal class WalletRepositoryImpl(
    private val userCredentialsMapper: UserCredentialsMapper,
    private val preferencesDataSource: IPreferencesDataSource,
    private val secretsDataSource: ISecretsDataSource,
    private val secretUtils: SecretUtils,
    private val walletDataSource: IWalletDataSource
): IWalletRepository {

    private companion object {
        const val WALLET_SECRET_LENGTH = 30
    }

    init {
        setupBouncyCastle()
    }

    override suspend fun loadCredentials(): UserWalletCredentials = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        val walletSecret = secretsDataSource.getByUserUid(userUid)
        val credentials = walletDataSource.loadCredentials(walletSecret.secret)
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: Exception) {
        throw DataRepositoryException("An error occurred when trying to load wallet credentials", ex)
    }

    override suspend fun generate(): UserWalletCredentials = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        // Generate Wallet Secret
        val walletSecret = secretUtils.generatePassword(
            isWithLetters = true,
            isWithSpecial = true,
            isWithNumbers = true,
            isWithUppercase = true,
            length = WALLET_SECRET_LENGTH
        )
        val credentials = walletDataSource.generate(walletSecret)
        secretsDataSource.save(SecretDTO(userUid, walletSecret))
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw DataRepositoryException("An error occurred when generating a new wallet", ex)
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}