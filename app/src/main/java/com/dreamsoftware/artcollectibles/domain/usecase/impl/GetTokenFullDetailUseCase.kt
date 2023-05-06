package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class GetTokenFullDetailUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCaseWithParams<GetTokenFullDetailUseCase.Params, ArtCollectible>() {

    override suspend fun onExecuted(params: Params): ArtCollectible =
        artCollectibleRepository.getTokenById(
            tokenId = params.tokenId,
            fullDetail = true
        )

    data class Params(
        val tokenId: BigInteger
    )
}