package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import com.dreamsoftware.artcollectibles.data.blockchain.contracts.FaucetContract
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IFaucetBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.exception.BlockchainDataSourceException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.NotEnoughFundsException
import com.dreamsoftware.artcollectibles.data.blockchain.exception.RequestSeedFundsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.FastRawTransactionManager

internal class FaucetBlockchainDataSourceImpl(
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j
): IFaucetBlockchainDataSource {

    override suspend fun requestSeedFunds(credentials: Credentials) {
        withContext(Dispatchers.IO) {
            try {
                with(loadContract(credentials)) {
                    val contractBalance = amount.send()
                    val initialAmount = initialAmount.send()
                    if(contractBalance < initialAmount) {
                        throw NotEnoughFundsException("Not enough funds available")
                    }
                    requestSeedFunds().send()
                }
            } catch (ex: BlockchainDataSourceException) {
                throw ex
            } catch (ex: Exception) {
                throw RequestSeedFundsException("An error occurred when trying to request seed funds", ex)
            }
        }
    }

    private fun loadContract(credentials: Credentials): FaucetContract = with(blockchainConfig) {
        val txManager = FastRawTransactionManager(web3j, credentials, chainId)
        FaucetContract.load(
            faucetContractAddress, web3j, txManager, gasProvider
        )
    }
}