package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class GetTokenDetailUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCaseWithParams<GetTokenDetailUseCase.Params, ArtCollectible>() {

    override suspend fun onExecuted(params: Params): ArtCollectible =
        artCollectibleRepository.getTokenById(params.tokenId)

    data class Params(
        val tokenId: BigInteger
    )
}