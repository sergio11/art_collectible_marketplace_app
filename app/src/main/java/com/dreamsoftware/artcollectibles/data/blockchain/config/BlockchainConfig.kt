package com.dreamsoftware.artcollectibles.data.blockchain.config

import com.dreamsoftware.artcollectibles.BuildConfig
import org.web3j.tx.gas.ContractEIP1559GasProvider
import org.web3j.tx.gas.StaticEIP1559GasProvider
import java.math.BigInteger

class BlockchainConfig {

    val alchemyUrl: String
        get() = BuildConfig.ALCHEMY_URL

    val artCollectibleContractAddress: String
        get() = BuildConfig.ART_COLLECTIBLE_CONTRACT_ADDRESS

    val artMarketplaceContractAddress: String
        get() = BuildConfig.ART_MARKETPLACE_CONTRACT_ADDRESS

    val faucetContractAddress: String
        get() = BuildConfig.FAUCET_CONTRACT_ADDRESS

    val chainId: Long
        get() = BuildConfig.CHAIN_ID

    val maxFeePerGas: BigInteger
        get() = BigInteger.valueOf(3500000000L)

    val maxPriorityFeePerGas: BigInteger
        get() = BigInteger.valueOf(3500000000L)

    val gasLimit: BigInteger
        get() = BigInteger.valueOf(BuildConfig.GAS_LIMIT)

    val gasProvider: ContractEIP1559GasProvider
        get() = StaticEIP1559GasProvider(chainId, maxFeePerGas, maxPriorityFeePerGas, gasLimit)

}