package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import android.content.Context
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import com.dreamsoftware.artcollectibles.data.preferences.datasource.IPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File

internal class WalletDataSourceImpl(
    private val appContext: Context,
    private val preferencesDataSource: IPreferencesDataSource
): IWalletDataSource {

    private companion object {
        const val INTERNAL_DIRECTORY_NAME = "wallets_internal_data"
    }

    @OptIn(FlowPreview::class)
    @Throws(LoadWalletCredentialsException::class)
    override suspend fun loadCredentials(): Flow<Credentials> =
        preferencesDataSource.getWalletPassword().flatMapConcat { loadCredentials(it) }

    @Throws(LoadWalletCredentialsException::class)
    override suspend fun loadCredentials(password: String): Flow<Credentials> = flow {
        withContext(Dispatchers.IO) {
            try {
                emit(WalletUtils.loadCredentials(password, getInternalWalletDirectory()))
            } catch (ex: Exception) {
                throw LoadWalletCredentialsException(
                    message = "An error occurred when loading credentials",
                    ex
                )
            }
        }
    }

    @Throws(GenerateWalletException::class)
    override suspend fun generate(password: String): Flow<String> = flow {
        withContext(Dispatchers.IO) {
            try {
                emit(WalletUtils.generateLightNewWalletFile(password, getInternalWalletDirectory()))
            } catch (ex: Exception) {
                throw GenerateWalletException(
                    message = "An error occurred when creating a new wallet file",
                    ex
                )
            }
        }
    }

    private fun getInternalWalletDirectory(): File = File(appContext.filesDir, INTERNAL_DIRECTORY_NAME).also {
        if (!it.exists()) {
            it.mkdir()
        }
    }
}