package com.dreamsoftware.artcollectibles.data.blockchain.crytocompare.service

import com.dreamsoftware.artcollectibles.data.blockchain.crytocompare.models.MaticPricesDTO
import retrofit2.http.GET

/**
 * Crypto Compare Service
 * ============================
 */
interface ICryptoCompareService {

    /**
     * Get Matic prices
     */
    @GET("data/price?fsym=MATIC&tsyms=USD,EUR")
    suspend fun getMaticPrices(): MaticPricesDTO

}