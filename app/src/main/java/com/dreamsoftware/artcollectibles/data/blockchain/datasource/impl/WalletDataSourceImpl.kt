package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import android.content.Context
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IWalletDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File

internal class WalletDataSourceImpl(
    private val appContext: Context
) : IWalletDataSource {

    private companion object {
        const val INTERNAL_DIRECTORY_NAME = "wallets_internal_data"
    }

    @Throws(LoadWalletCredentialsException::class)
    override suspend fun loadCredentials(password: String): Credentials =
        withContext(Dispatchers.IO) {
            try {
                WalletUtils.loadCredentials(password, getInternalWalletDirectory())
            } catch (ex: Exception) {
                throw LoadWalletCredentialsException(
                    message = "An error occurred when loading credentials",
                    ex
                )
            }
        }

    @Throws(GenerateWalletException::class)
    override suspend fun generate(password: String): Credentials = withContext(Dispatchers.IO) {
        try {
            val walletName = WalletUtils.generateLightNewWalletFile(password, getInternalWalletDirectory())
            WalletUtils.loadCredentials(password, File(getInternalWalletDirectory(), walletName))
        } catch (ex: Exception) {
            throw GenerateWalletException(
                message = "An error occurred when creating a new wallet file",
                ex
            )
        }
    }

    private fun getInternalWalletDirectory(): File =
        File(appContext.filesDir, INTERNAL_DIRECTORY_NAME).also {
            if (!it.exists()) {
                it.mkdir()
            }
        }
}