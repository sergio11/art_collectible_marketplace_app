package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class WithdrawFromSaleUseCase(
    private val artMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<WithdrawFromSaleUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        artMarketplaceRepository.withdrawFromSale(params.tokenId)
    }

    data class Params(
        val tokenId: BigInteger
    )

}