package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class BuyItemUseCase(
    private val artCollectibleMarketplaceRepository: IArtMarketplaceRepository
): BaseUseCaseWithParams<BuyItemUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            artCollectibleMarketplaceRepository.buyItem(tokenId)
        }
    }

    data class Params(
        val tokenId: BigInteger
    )
}