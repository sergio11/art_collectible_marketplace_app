package com.dreamsoftware.artcollectibles.data.blockchain.datasource.impl

import com.dreamsoftware.artcollectibles.data.blockchain.crytocompare.service.ICryptoCompareService
import com.dreamsoftware.artcollectibles.data.blockchain.datasource.IMarketPricesBlockchainDataSource
import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketPricesDTO
import com.dreamsoftware.artcollectibles.data.core.network.SupportNetworkDataSource
import org.web3j.protocol.Web3j
import java.math.BigDecimal

/**
 * Market Prices blockchain data source
 * @param cryptoCompareService
 * @param web3j
 */
internal class MarketPricesBlockchainDataSourceImpl(
    private val cryptoCompareService: ICryptoCompareService,
    private val web3j: Web3j
): SupportNetworkDataSource(), IMarketPricesBlockchainDataSource {

    override suspend fun getPricesOf(priceInEth: BigDecimal): MarketPricesDTO = safeNetworkCall {
        val maticPrices = cryptoCompareService.getMaticPrices()
        MarketPricesDTO(
            priceInEUR = priceInEth.toDouble() * maticPrices.priceEUR,
            priceInUSD = priceInEth.toDouble() * maticPrices.priceUSD
        )
    }
}