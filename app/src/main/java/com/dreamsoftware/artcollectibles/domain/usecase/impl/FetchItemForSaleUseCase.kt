package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtMarketplaceRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class FetchItemForSaleUseCase(
    private val artCollectibleMarketplaceRepository: IArtMarketplaceRepository
) : BaseUseCaseWithParams<FetchItemForSaleUseCase.Params, ArtCollectibleForSale>() {

    override suspend fun onExecuted(params: Params): ArtCollectibleForSale =
        artCollectibleMarketplaceRepository.fetchItemForSale(tokenId = params.tokenId)

    data class Params(
        val tokenId: BigInteger
    )
}