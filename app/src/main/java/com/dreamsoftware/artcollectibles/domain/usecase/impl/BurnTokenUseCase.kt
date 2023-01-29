package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class BurnTokenUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
): BaseUseCaseWithParams<BurnTokenUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        artCollectibleRepository.delete(params.tokenId)
    }

    data class Params(
        val tokenId: BigInteger
    )
}