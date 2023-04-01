package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class PutItemForSaleUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<PutItemForSaleUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params): Unit = with(params) {
        artMarketplaceRepository.putItemForSale(tokenId, priceInEth)
    }

    data class Params(
        val tokenId: BigInteger,
        val priceInEth: Float
    )
}