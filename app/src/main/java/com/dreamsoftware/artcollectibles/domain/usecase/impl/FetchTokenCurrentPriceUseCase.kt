package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectiblePrices
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class FetchTokenCurrentPriceUseCase(
    private val artCollectibleMarketplaceRepository: IArtMarketplaceRepository
) : BaseUseCaseWithParams<FetchTokenCurrentPriceUseCase.Params, ArtCollectiblePrices>() {

    override suspend fun onExecuted(params: Params): ArtCollectiblePrices =
        artCollectibleMarketplaceRepository.fetchTokenCurrentPrice(tokenId = params.tokenId)

    data class Params(
        val tokenId: BigInteger
    )
}