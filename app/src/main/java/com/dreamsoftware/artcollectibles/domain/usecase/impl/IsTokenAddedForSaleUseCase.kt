package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class IsTokenAddedForSaleUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<IsTokenAddedForSaleUseCase.Params, Boolean>() {

    override suspend fun onExecuted(params: Params): Boolean =
        artMarketplaceRepository.isTokenAddedForSale(params.tokenId)

    data class Params(
        val tokenId: BigInteger
    )
}