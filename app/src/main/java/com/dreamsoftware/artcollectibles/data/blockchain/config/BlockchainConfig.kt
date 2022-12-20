package com.dreamsoftware.artcollectibles.data.blockchain.config

import com.dreamsoftware.artcollectibles.BuildConfig
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

class BlockchainConfig {

    val alchemyUrl: String
        get() = BuildConfig.ALCHEMY_URL

    val artCollectibleContractAddress: String
        get() = BuildConfig.ART_COLLECTIBLE_CONTRACT_ADDRESS

    val artMarketplaceContractAddress: String
        get() = BuildConfig.ART_MARKETPLACE_CONTRACT_ADDRESS

    val chainId: Long
        get() = BuildConfig.CHAIN_ID

    val gasProvider: StaticGasProvider
        get() = StaticGasProvider(
            BigInteger.valueOf(BuildConfig.GAS_PRICE),
            BigInteger.valueOf(BuildConfig.GAS_LIMIT))
}