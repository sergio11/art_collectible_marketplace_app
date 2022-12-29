package com.dreamsoftware.artcollectibles.data.api.repository.impl

import com.dreamsoftware.artcollectibles.data.api.exception.DataRepositoryException
import com.dreamsoftware.artcollectibles.data.api.mapper.UserCredentialsMapper
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.firebase.datasource.IWalletSecretsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.model.WalletSecretDTO
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import com.dreamsoftware.artcollectibles.utils.SecretUtils

/**
 * Wallet Repository Implementation
 * @param userCredentialsMapper
 * @param preferencesDataSource
 * @param walletSecretsDataSource
 * @param walletDataSource
 */
internal class WalletRepositoryImpl(
    private val userCredentialsMapper: UserCredentialsMapper,
    private val preferencesDataSource: IPreferencesDataSource,
    private val walletSecretsDataSource: IWalletSecretsDataSource,
    private val secretUtils: SecretUtils,
    private val walletDataSource: IWalletDataSource
): IWalletRepository {

    private companion object {
        const val WALLET_SECRET_LENGTH = 30
    }

    override suspend fun loadCredentials(): UserWalletCredentials = try {
        val userUid = preferencesDataSource.getAuthUserUid()
        val walletSecret = walletSecretsDataSource.getByUserUid(userUid)
        val credentials = walletDataSource.loadCredentials(walletSecret.name, walletSecret.secret)
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
        val walletName = walletDataSource.generate(walletSecret)
        walletSecretsDataSource.save(WalletSecretDTO(userUid, walletName, walletSecret))
        val credentials = walletDataSource.loadCredentials(walletName, walletSecret)
        userCredentialsMapper.mapInToOut(credentials)
    } catch (ex: Exception) {
        throw DataRepositoryException("An error occurred when generating a new wallet", ex)
    }
}