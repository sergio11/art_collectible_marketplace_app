package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.GenerateWalletException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.LoadWalletCredentialsException
import org.web3j.crypto.Credentials

interface IWalletDataSource {

    @Throws(LoadWalletCredentialsException::class)
    fun loadCredentials(password: String): Credentials

    @Throws(GenerateWalletException::class)
    fun generate(password: String): String

}