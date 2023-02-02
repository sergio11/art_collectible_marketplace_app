package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class PutItemForSaleUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<PutItemForSaleUseCase.Params, BigInteger>() {

    override suspend fun onExecuted(params: Params): BigInteger = with(params) {
        artMarketplaceRepository.putItemForSale(tokenId, price)
    }

    data class Params(
        val tokenId: BigInteger,
        val price: BigInteger
    )
}