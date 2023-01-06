package com.dreamsoftware.artcollectibles.data.blockchain.alchemy.service

import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.models.request.AlchemyRequestDTO
import com.dreamsoftware.artcollectibles.data.blockchain.alchemy.models.response.AlchemyResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Account Information API
 * =======================
 */
interface IAccountInformationService {

    /**
     * Get Native Balance
     * @param request
     */
    @POST(" ")
    suspend fun getNativeBalance(@Body request: AlchemyRequestDTO): AlchemyResponseDTO
}