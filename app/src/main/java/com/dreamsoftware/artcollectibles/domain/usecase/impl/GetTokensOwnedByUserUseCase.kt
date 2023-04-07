package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class GetTokensOwnedByUserUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository
) : BaseUseCaseWithParams<GetTokensOwnedByUserUseCase.Params, Iterable<ArtCollectible>>() {

    override suspend fun onExecuted(params: Params): Iterable<ArtCollectible> = with(params) {
        with(artCollectibleRepository) {
            limit?.let { getTokensOwnedBy(userAddress, it) } ?: getTokensOwnedBy(userAddress)
        }
    }

    data class Params(
        val userAddress: String,
        val limit: Long? = null
    )
}