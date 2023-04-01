package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.model.MarketPricesDTO
import java.math.BigDecimal

interface IMarketPricesBlockchainDataSource {

    suspend fun getPricesOf(priceInEth: BigDecimal): MarketPricesDTO

}