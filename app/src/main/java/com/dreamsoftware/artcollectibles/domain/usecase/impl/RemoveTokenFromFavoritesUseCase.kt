package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IFavoritesRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class RemoveTokenFromFavoritesUseCase(
    private val favoritesRepository: IFavoritesRepository
): BaseUseCaseWithParams<RemoveTokenFromFavoritesUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            favoritesRepository.remove(tokenId, userAddress)
        }
    }

    data class Params(
        val tokenId: String,
        val userAddress: String
    )
}