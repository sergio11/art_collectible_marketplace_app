package com.dreamsoftware.artcollectibles.data.blockchain.datasource.core

import com.dreamsoftware.artcollectibles.data.blockchain.config.BlockchainConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.math.BigInteger

abstract class SupportBlockchainDataSource(
    private val blockchainConfig: BlockchainConfig,
    private val web3j: Web3j
) {

    protected suspend fun sendTransaction(encodeFunctionCall: String, credentials: Credentials, target: String): String =
        withContext(Dispatchers.IO) {
            with(blockchainConfig) {
                val nonce = getTxnNonce(credentials)
                val transaction1559 = RawTransaction.createTransaction(
                    chainId,
                    nonce,
                    gasLimit,
                    target,
                    BigInteger.ZERO,
                    encodeFunctionCall,
                    maxFeePerGas,
                    maxPriorityFeePerGas
                )
                val signedMessage = TransactionEncoder.signMessage(transaction1559, credentials)
                val hexValue = Numeric.toHexString(signedMessage)
                val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()
                ethSendTransaction.transactionHash
            }
        }

    private suspend fun getTxnNonce(credentials: Credentials): BigInteger =
        withContext(Dispatchers.IO) {
            val ethGetTransactionCount =
                web3j.ethGetTransactionCount(credentials.address, DefaultBlockParameterName.LATEST)
                    .sendAsync().get()
            ethGetTransactionCount.transactionCount
        }
}