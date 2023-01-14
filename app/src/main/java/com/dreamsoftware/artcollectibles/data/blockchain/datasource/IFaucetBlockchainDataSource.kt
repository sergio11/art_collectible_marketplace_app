package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.NotEnoughFundsException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.RequestSeedFundsException
import org.web3j.crypto.Credentials

interface IFaucetBlockchainDataSource {

    /**
     * Request seed funds
     * @param credentials
     */
    @Throws(NotEnoughFundsException::class, RequestSeedFundsException::class)
    suspend fun requestSeedFunds(credentials: Credentials)
}